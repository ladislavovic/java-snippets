#!/bin/bash

#
# This file runs the "spring-boot-opentelemetry" application with opentelemetry java agent.
# It just print all information to the standard output, there is no connection to any
# external tool
#

# Everything in this variable is automatically prepended to the JVM command line every time a Java application is launched
# It is a way how to set up JVM options globally
export JAVA_TOOL_OPTIONS="-javaagent:opentelemetry-javaagent-2-15-0.jar"

# Traces, logs and metrics are exported to the standard output
# Another possible values are:
#   none - not exported at all
#   otlp - exported by OTLP protocol, then it could be easily exported to another tool
#          like DataDog or Prometheus
export OTEL_TRACES_EXPORTER=logging
export OTEL_METRICS_EXPORTER=logging
export OTEL_LOGS_EXPORTER=logging

# It is 60 seconds by default, for these experiments it is better to shorten the interval
export OTEL_METRIC_EXPORT_INTERVAL=15000

# Environment name the application is running on
export OTEL_SERVICE_ENVIRONMENT=local

# Name of the monitored service
export OTEL_SERVICE_NAME=kul-spring-boot-opentelemetry

# And finaly, execute the app and look at the standard output!
java -jar ./target/spring-boot-opentelemetry-0.0.1-SNAPSHOT.jar
