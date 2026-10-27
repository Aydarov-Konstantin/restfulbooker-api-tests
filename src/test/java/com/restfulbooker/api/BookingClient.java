package com.restfulbooker.api;

import com.restfulbooker.model.Booking;
import com.restfulbooker.model.Credentials;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingClient {

    @Step("Get all booking identifiers")
    public Response getBookingIds() {
        return given().when().get("/booking");
    }

    @Step("Get booking with ID {bookingId}")
    public Response getBooking(int bookingId) {
        return given().pathParam("bookingId", bookingId).when().get("/booking/{bookingId}");
    }

    @Step("Create a booking")
    public Response createBooking(Booking booking) {
        return given().body(booking).when().post("/booking");
    }

    @Step("Update booking with ID {bookingId}")
    public Response updateBooking(int bookingId, Booking booking, String token) {
        return given()
                .pathParam("bookingId", bookingId)
                .cookie("token", token)
                .body(booking)
                .when().put("/booking/{bookingId}");
    }

    @Step("Partially update booking with ID {bookingId}")
    public Response updateBookingName(int bookingId, String firstName, String token) {
        return given()
                .pathParam("bookingId", bookingId)
                .cookie("token", token)
                .body("{\"firstname\":\"" + firstName + "\"}")
                .when().patch("/booking/{bookingId}");
    }

    @Step("Delete booking with ID {bookingId}")
    public Response deleteBooking(int bookingId, String token) {
        return given()
                .pathParam("bookingId", bookingId)
                .cookie("token", token)
                .when().delete("/booking/{bookingId}");
    }

    @Step("Authenticate user {credentials.username}")
    public Response authenticate(Credentials credentials) {
        return given().body(credentials).when().post("/auth");
    }
}
