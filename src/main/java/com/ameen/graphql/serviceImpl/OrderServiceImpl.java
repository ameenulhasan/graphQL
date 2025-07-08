package com.ameen.graphql.serviceImpl;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ameen.graphql.constant.Constant;
import com.ameen.graphql.dto.OrderDto;
import com.ameen.graphql.dto.OrderIdDto;
import com.ameen.graphql.dto.UserOrderStatsDto;
import com.ameen.graphql.exception.CustomGraphQLException;
import com.ameen.graphql.model.Book;
import com.ameen.graphql.model.Order;
import com.ameen.graphql.model.User;
import com.ameen.graphql.repository.BookRepository;
import com.ameen.graphql.repository.OrderRepository;
import com.ameen.graphql.repository.UserRepository;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.response.UserContextHolder;
import com.ameen.graphql.service.OrderService;
import com.lowagie.text.Document;
import io.jsonwebtoken.io.IOException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Base64;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public SuccessResponse<Object> saveOrder(OrderDto orderDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new CustomGraphQLException(Constant.USER_NOT_FOUND));
        Book book = bookRepository.findById(orderDto.getBookId())
                .orElseThrow(() -> new CustomGraphQLException("Book not found"));
        Integer quantity = orderDto.getQuantity();
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        long bookPrice;
        try {
            bookPrice = Long.parseLong(book.getPrice());
        } catch (NumberFormatException e) {
            throw new CustomGraphQLException("Invalid book price format. Must be a number.");
        }
        long total = bookPrice * quantity;
        Order order;
        boolean isUpdate = false;
        order = orderRepository.findById(orderDto.getId())
                .orElseThrow(() -> new CustomGraphQLException("Order not found"));
        order.setIsActive(true);
        order.setDeletedFlag(false);
        order.setModifiedAt(Timestamp.from(Instant.now()));
        order.setModifiedBy(userId);
        isUpdate = true;
        order.setUser(user);
        order.setBook(book);
        order.setQuantity(quantity);
        order.setTotalPrice(total);
        order.setDate(order.getDate());
        order.setIsActive(true);
        order.setDeletedFlag(false);
        orderRepository.save(order);
        successResponse.setStatusMessage(isUpdate ? "Order updated successfully" : "Order created successfully");
        successResponse.setData(order);
        successResponse.setStatusCode(200);
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> getOrderById(Long orderId) {
        SuccessResponse<Object> response = new SuccessResponse<>();
        Order order = orderRepository.findByIdWithUserAndBook(orderId)
                .orElseThrow(() -> new CustomGraphQLException("Order not found with ID: " + orderId));
        OrderIdDto dto = new OrderIdDto();
        dto.setOrderId(order.getId());
        dto.setQuantity(order.getQuantity());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setUserName(order.getUser().getName());
        dto.setBookName(order.getBook().getTitle());
        dto.setAuthor(order.getBook().getAuthor());
        dto.setPrice(order.getBook().getPrice());
        response.setStatusCode(200);
        response.setStatusMessage("Order fetched successfully");
        response.setData(dto);
        return response;
    }

    @Override
    public SuccessResponse<Object> getUserOrderStats(Long userId) {
        SuccessResponse<Object> response = new SuccessResponse<>();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomGraphQLException("User not found"));
        List<Order> orders = orderRepository.findByUserId(userId);
        long totalBooks = orders.size();
        long totalSpent = orders.stream().mapToLong(Order::getTotalPrice).sum();
        List<UserOrderStatsDto.BookDetailsDto> bookDetails = orders.stream()
                .map(order -> {
                    Book book = order.getBook();
                    return new UserOrderStatsDto.BookDetailsDto(
                            book.getTitle(),
                            book.getAuthor(),
                            book.getPrice()
                    );
                }).distinct().toList();
        UserOrderStatsDto dto = new UserOrderStatsDto(
                user.getId(),
                user.getName(),
                totalBooks,
                totalSpent,
                bookDetails
        );
        response.setStatusMessage("User order stats fetched successfully");
        response.setData(dto);
        return response;
    }

    @Override
    public String orderDownload(String format, String date, String month) {
        byte[] fileBytes;
        if ("excel".equalsIgnoreCase(format)) {
            fileBytes = generateExcel(date, month);
        } else if ("pdf".equalsIgnoreCase(format)) {
            fileBytes = generatePdf(date, month);
        } else {
            throw new CustomGraphQLException("Invalid format requested. Use 'pdf' or 'excel'");
        }
        return Base64.getEncoder().encodeToString(fileBytes);
    }
    private byte[] generatePdf(String date, String month) {
        try {
            List<Order> orders = orderRepository.findByDateMonth(date, month);
            if (orders == null || orders.isEmpty()) {
                throw new CustomGraphQLException("No orders found for given date and month");
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfPTable table = new PdfPTable(7);
            table.addCell("ID");
            table.addCell("User Name");
            table.addCell("Author");
            table.addCell("Title");
            table.addCell("Price");
            table.addCell("Quantity");
            table.addCell("Total Price");
            for (Order order : orders) {
                table.addCell(String.valueOf(order.getId()));
                table.addCell(order.getUser().getName());
                table.addCell(order.getBook().getAuthor());
                table.addCell(order.getBook().getTitle());
                table.addCell(String.valueOf(order.getBook().getPrice()));
                table.addCell(String.valueOf(order.getQuantity()));
                table.addCell(String.valueOf(order.getTotalPrice()));
            }
            document.add(table);
            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating PDF", e);
        }
    }
    private byte[] generateExcel(String date, String month) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Orders");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("User Name");
            headerRow.createCell(2).setCellValue("Author");
            headerRow.createCell(3).setCellValue("Title");
            headerRow.createCell(4).setCellValue("Price");
            headerRow.createCell(5).setCellValue("Quantity");
            headerRow.createCell(6).setCellValue("Total Price");
            List<Order> orders = orderRepository.findByDateMonth(date, month);
            int rowNum = 1;
            for (Order order : orders) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(order.getId());
                row.createCell(1).setCellValue(order.getUser().getName());
                row.createCell(2).setCellValue(order.getBook().getAuthor());
                row.createCell(3).setCellValue(order.getBook().getTitle());
                row.createCell(4).setCellValue(order.getBook().getPrice());
                row.createCell(5).setCellValue(order.getQuantity());
                row.createCell(6).setCellValue(order.getTotalPrice().doubleValue());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Error generating Excel file", e);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

}
