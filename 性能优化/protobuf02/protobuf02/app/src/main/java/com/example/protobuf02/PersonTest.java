package com.example.protobuf02;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.Arrays;

public class PersonTest {

     public static void main(String ... args){

          //序列化
          Person._Person._PhoneNumber.Builder phoneNumberBuilder
                  = Person._Person._PhoneNumber.newBuilder()
                  .setNumber("18670301864");

          Person._Person.Builder personBuilder
                  = Person._Person.newBuilder()
                  .setName("Zero")
                  .setEmail("33322@qq.com")
                  .setId(12)
                  .addPhone(phoneNumberBuilder);

          Person._Person person = personBuilder.build();

          byte[] bytes = person.toByteArray();
          System.out.println(Arrays.toString(bytes));
          //反序列化

          try {
               Person._Person person1 = Person._Person.parseFrom(bytes);
               System.out.println(person1);
          } catch (InvalidProtocolBufferException e) {
               e.printStackTrace();
          }


     }

}
