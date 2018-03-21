package tech.iooo.coco;

import java.io.FileInputStream;
import tech.iooo.coco.protobuf.message.AddressBookProto;

/**
 * Created on 2018/3/20 下午10:07
 *
 * @author Ivan97
 */
public class ListPeople {

  // 打印地址簿中所有联系人信息
  static void print(AddressBookProto.AddressBook addressBook) {
    for (AddressBookProto.Person person : addressBook.getPeopleList()) {
      System.out.println("Person ID: " + person.getId());
      System.out.println("  Name: " + person.getName());
      if (!person.getPhonesList().isEmpty()) {
        System.out.println("  E-mail address: " + person.getEmail());
      }

      for (AddressBookProto.Person.PhoneNumber phoneNumber : person.getPhonesList()) {
        switch (phoneNumber.getType()) {
          case MOBILE:
            System.out.print("  Mobile phone #: ");
            break;
          case HOME:
            System.out.print("  Home phone #: ");
            break;
          case WORK:
            System.out.print("  Work phone #: ");
            break;
          default:
            break;
        }
        System.out.println(phoneNumber.getNumber());
      }
    }
  }

  // 加载指定的序列化文件，并输出所有联系人信息
  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage:  ListPeople ADDRESS_BOOK_FILE");
      System.exit(-1);
    }

    // Read the existing address book.
    AddressBookProto.AddressBook addressBook =
        AddressBookProto.AddressBook.parseFrom(new FileInputStream(args[0]));

    print(addressBook);
  }
}
