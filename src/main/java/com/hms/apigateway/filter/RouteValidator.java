//package com.hms.apigateway.filter;
//
//import java.util.List;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.http.server.PathContainer;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.pattern.PathPattern;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//@Component
//public class RouteValidator {
//	private static final Logger LOGGER = LogManager.getLogger(RouteValidator.class);
//    public static final List<String> openApiEndpoints = List.of(
//            "/hms/login/user-login",
//            "/hms/login/forgot-password/**");
// 
//    private  List<PathPattern> pathPatterns;
//    public RouteValidator() {
//        PathPatternParser parser = new PathPatternParser();
//        this.pathPatterns = openApiEndpoints.stream()
//                .map(parser::parse)
//                .collect(Collectors.toList());
//    }
//    
//    public Predicate<ServerHttpRequest> isSecured = request -> {
//        PathContainer requestPath = PathContainer.parsePath(request.getURI().getPath());
//        LOGGER.info("Incoming request URI: {}", requestPath);
//        return pathPatterns.stream().noneMatch(pattern -> pattern.matches(requestPath));
//    };
// 
// 
//     
//}