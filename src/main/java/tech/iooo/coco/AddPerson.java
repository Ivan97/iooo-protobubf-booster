package tech.iooo.coco;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import tech.iooo.coco.protobuf.message.AddressBookProto;

/**
 * Hello world!
 */
public class AddPerson {

  // 通过用户输入构建一个Person对象
  static AddressBookProto.Person promptForAddress(BufferedReader stdin,
      PrintStream stdout) throws IOException {
    AddressBookProto.Person.Builder person = AddressBookProto.Person.newBuilder();
    stdout.print("Enter person ID: ");
    person.setId(Integer.valueOf(stdin.readLine()));
    stdout.print("Enter name: ");
    person.setName(stdin.readLine());
    stdout.print("Enter email address (blank for none): ");
    String email = stdin.readLine();
    if (email.length() > 0) {
      person.setEmail(email);
    }
    while (true) {
      stdout.print("Enter a phone number (or leave blank to finish): ");
      String number = stdin.readLine();
      if (number.length() == 0) {
        break;
      }
      AddressBookProto.Person.PhoneNumber.Builder phoneNumber =
          AddressBookProto.Person.PhoneNumber.newBuilder().setNumber(number);
      stdout.print("Is this a mobile, home, or work phone? ");
      String type = stdin.readLine();
      if (type.equals("mobile")) {
        phoneNumber.setType(AddressBookProto.Person.PhoneType.MOBILE);
      } else if (type.equals("home")) {
        phoneNumber.setType(AddressBookProto.Person.PhoneType.HOME);
      } else if (type.equals("work")) {
        phoneNumber.setType(AddressBookProto.Person.PhoneType.WORK);
      } else {
        stdout.println("Unknown phone type.  Using default.");
      }
      person.addPhones(phoneNumber);
    }
    return person.build();
  }

  // 加载指定的序列化文件（如不存在则创建一个新的），再通过用户输入增加一个新的联系人到地址簿，最后序列化到文件中
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage:  AddPerson ADDRESS_BOOK_FILE");
      System.exit(-1);
    }
    AddressBookProto.AddressBook.Builder addressBook = AddressBookProto.AddressBook.newBuilder();
    // Read the existing address book.
    try {
      addressBook.mergeFrom(new FileInputStream(args[0]));
    } catch (FileNotFoundException e) {
      System.out.println(args[0] + ": File not found.  Creating a new file.");
    }
    // Add an address.
    addressBook.addPeople(promptForAddress(new BufferedReader(new InputStreamReader(System.in)),
        System.out));
    // Write the new address book back to disk.
    FileOutputStream output = new FileOutputStream(args[0]);
    addressBook.build().writeTo(output);
    output.close();
  }
}
