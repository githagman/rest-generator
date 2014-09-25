package com.hagman.spark;

import spark.Request;
import spark.Response;
import spark.RouteImpl;

/**
 * To be used for testing
 * Created by tom on 9/24/14.
 */
public class SparkExample {
    public RouteImpl get() {
        return new RouteImpl("test/:id") {
            @Override
            public Object handle(Request request, Response response) {
                //add application logic to get routes
                return "Created test";
            }
        };
    }
    public RouteImpl post() {
        return new RouteImpl("/tests") {
            @Override
            public Object handle(Request request, Response response) {
                //add application logic to get routes
                return "Added test";
            }
        };
    }
    public RouteImpl put() {
        return new RouteImpl("/test/:id") {
            @Override
            public Object handle(Request request, Response response) {
                //add application logic to get routes
                return "Updated test";
            }
        };
    }
    public RouteImpl delete() {
        return new RouteImpl("/test/:id") {
            @Override
            public Object handle(Request request, Response response) {
                //add application logic to get routes
                return "Deleted test";
            }
        };
    }
}
