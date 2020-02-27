/*
 * Influx API Service
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: 0.1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package com.influxdb.client.domain;

import java.util.Objects;
import java.util.Arrays;
import com.influxdb.client.domain.ArrayExpression;
import com.influxdb.client.domain.BinaryExpression;
import com.influxdb.client.domain.BooleanLiteral;
import com.influxdb.client.domain.CallExpression;
import com.influxdb.client.domain.ConditionalExpression;
import com.influxdb.client.domain.DateTimeLiteral;
import com.influxdb.client.domain.Duration;
import com.influxdb.client.domain.DurationLiteral;
import com.influxdb.client.domain.Expression;
import com.influxdb.client.domain.FloatLiteral;
import com.influxdb.client.domain.FunctionExpression;
import com.influxdb.client.domain.Identifier;
import com.influxdb.client.domain.IndexExpression;
import com.influxdb.client.domain.IntegerLiteral;
import com.influxdb.client.domain.LogicalExpression;
import com.influxdb.client.domain.MemberExpression;
import com.influxdb.client.domain.Node;
import com.influxdb.client.domain.ObjectExpression;
import com.influxdb.client.domain.ParenExpression;
import com.influxdb.client.domain.PipeExpression;
import com.influxdb.client.domain.PipeLiteral;
import com.influxdb.client.domain.Property;
import com.influxdb.client.domain.PropertyKey;
import com.influxdb.client.domain.RegexpLiteral;
import com.influxdb.client.domain.StringLiteral;
import com.influxdb.client.domain.UnaryExpression;
import com.influxdb.client.domain.UnsignedIntegerLiteral;
import java.util.List;

/**
 * Expression
 */

public class Expression extends Node {

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode());
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Expression {\n");
    sb.append("    ").append(toIndentedString(super.toString())).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
