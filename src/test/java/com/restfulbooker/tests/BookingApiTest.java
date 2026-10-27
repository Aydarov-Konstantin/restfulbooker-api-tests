package com.restfulbooker.tests;

import com.restfulbooker.api.BookingClient;
import com.restfulbooker.config.ApiConfig;
import com.restfulbooker.model.Booking;
import com.restfulbooker.model.BookingDates;
import com.restfulbooker.model.Credentials;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful Booker API")
@Feature("Booking management")
@Owner("Konstantin")
class BookingApiTest {
    private static final BookingClient bookingClient = new BookingClient();

    @BeforeAll
    static void setUp() {
        ApiConfig.configure();
    }

    @Test
    @Story("Service availability")
    @Severity(SeverityLevel.BLOCKER)
    @Description("The health-check endpoint confirms that the API is available.")
    void shouldConfirmApiIsAvailable() {
        int statusCode = bookingClient.getBookingIds().statusCode();

        assertThat(statusCode).isEqualTo(200);
    }

    @Test
    @Story("Booking retrieval")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnBookingIdentifiers() {
        List<Integer> bookingIds = bookingClient.getBookingIds().jsonPath().getList("bookingid", Integer.class);

        assertThat(bookingIds).isNotNull().isNotEmpty().doesNotContainNull();
    }

    @Test
    @Story("Booking retrieval")
    @Severity(SeverityLevel.CRITICAL)
    void shouldReturnBookingDetails() {
        int bookingId = bookingClient.getBookingIds().jsonPath().getInt("bookingid[0]");
        Map<String, Object> booking = bookingClient.getBooking(bookingId).then().statusCode(200).extract().as(Map.class);

        assertThat(booking)
                .containsKeys("firstname", "lastname", "totalprice", "depositpaid", "bookingdates")
                .extractingByKey("bookingdates")
                .isInstanceOf(Map.class);
    }

    @Test
    @Story("Authentication")
    @Severity(SeverityLevel.BLOCKER)
    void shouldCreateAuthenticationToken() {
        String token = bookingClient.authenticate(new Credentials("admin", "password123"))
                .then().statusCode(200).extract().path("token");

        assertThat(token).isNotBlank();
    }

    @Test
    @Story("Booking lifecycle")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Creates, updates, partially updates, retrieves, and deletes a booking.")
    void shouldManageBookingLifecycle() {
        String token = bookingClient.authenticate(new Credentials("admin", "password123"))
                .then().statusCode(200).extract().path("token");
        Booking initialBooking = booking("Alex", "Tester", 150, true, "Breakfast");
        int bookingId = bookingClient.createBooking(initialBooking)
                .then().statusCode(200).extract().path("bookingid");

        try {
            Booking updatedBooking = booking("Alex", "Automation", 220, false, "Late checkout");
            Booking updateResult = bookingClient.updateBooking(bookingId, updatedBooking, token)
                    .then().statusCode(200).extract().as(Booking.class);
            assertThat(updateResult).isEqualTo(updatedBooking);

            bookingClient.updateBookingName(bookingId, "Alexandra", token).then().statusCode(200);
            Booking retrievedBooking = bookingClient.getBooking(bookingId)
                    .then().statusCode(200).extract().as(Booking.class);
            assertThat(retrievedBooking.firstname()).isEqualTo("Alexandra");
            assertThat(retrievedBooking.additionalneeds()).isEqualTo("Late checkout");
        } finally {
            bookingClient.deleteBooking(bookingId, token).then().statusCode(201);
        }

        assertThat(bookingClient.getBooking(bookingId).statusCode()).isEqualTo(404);
    }

    @Test
    @Story("Error handling")
    @Severity(SeverityLevel.NORMAL)
    void shouldReturnNotFoundForUnknownBooking() {
        int statusCode = bookingClient.getBooking(Integer.MAX_VALUE).statusCode();

        assertThat(statusCode).isEqualTo(404);
    }

    private Booking booking(String firstName, String lastName, int totalPrice, boolean depositPaid, String additionalNeeds) {
        return new Booking(
                firstName,
                lastName,
                totalPrice,
                depositPaid,
                new BookingDates("2026-12-01", "2026-12-10"),
                additionalNeeds
        );
    }
}
