package assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Buyer extends User {

    private int rating;

    public Buyer(String id, String username, String password, String name, String emailAddress, String ContactNo, String address, int rating) {
        super(id, username, password, name, emailAddress, ContactNo, address);
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public ArrayList<Item> viewItemsOnSale() {
        int counter = 1;
        ArrayList<Item> items = MyReader.getItems();
        ArrayList<Item> myItems = new ArrayList();
        for (Item item : items) {
            if (item.getAvalibility()==true) {
                myItems.add(item);
                counter++;
            }
        }
        return myItems;
    }


    public ArrayList<Item> viewItemsByCategory(String category) {

        ArrayList<Item> items = MyReader.getItems();
        ArrayList<Item> myItems = new ArrayList<Item>();
        for (Item item : items) {
            if ((category.equals("All Categories")&& (item.getAvalibility()==true)) || (item.getItemCategory().equals(category)&& (item.getAvalibility() == true))){
                myItems.add(item);
            }
        }

        return myItems;
    }

    public void purchaseItem(Item item) {

        File buy = new File("PurchasedItems.txt");
        String line = item.toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(buy, true))) {

            writer.write(getId() + "-" + line + System.getProperty("line.separator"));
            item.setBought();
            Seller s = MyReader.getSellers().get(item.getSellerId() - 1);
            s.deductSeccuessFees(Item.calculateSucessFees(item.getItemPrice()));
            incrementRating();
        } catch (Exception e) {
            System.out.println("Files can not be opened");
        }

    }

    public ArrayList<BoughtItem> viewOwnBoughtItems() {
        ArrayList<BoughtItem> items = MyReader.getPurchasedItems();
        ArrayList<BoughtItem> myItems = new ArrayList<BoughtItem>();
        for (BoughtItem item : items) {
            String[] parts = item.toString().split("-");
            if (String.valueOf(parts[0]).equals(getId())) {
                myItems.add(item);
            }

        }
        return myItems;
    }

    public void editDetails(String name, String password, String emailAddress, String address, String contactNo) {
        try {

            BufferedReader br = new BufferedReader(new FileReader("Buyer.txt"));
            String currentLine;

            StringBuilder fileContent = new StringBuilder();
            //Read File Line By Line
            while ((currentLine = br.readLine()) != null) {

                String parts[] = currentLine.split("-");
                if (parts.length > 0) {
                    // Here parts[0] will have value of ID
                    if (parts[0].equals(getId())) {
                        parts[2] = password;
                        parts[3] = name;
                        parts[4] = emailAddress;
                        parts[5] = contactNo;
                        parts[6] = address;
                        String newLine = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3] + "-" + parts[4] + "-" + parts[5] + "-" + parts[6] + "-" + parts[7];
                        fileContent.append(newLine);
                        fileContent.append("\n");
                    } else {
                        // update content as it is
                        fileContent.append(currentLine);
                        fileContent.append("\n");
                    }
                }
            }
            // Now fileContent will have updated content , which you can override into file
            FileWriter w = new FileWriter("Buyer.txt");
            BufferedWriter out = new BufferedWriter(w);
            out.write(fileContent.toString());
            out.close();
            //Close the input stream
            br.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        
    }

    public void incrementRating() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("Buyer.txt"));
            String strLine;
            StringBuilder fileContent = new StringBuilder();
            int counter = 1;
            while ((strLine = br.readLine()) != null) {
                String parts[] = strLine.split("-");
                if (parts.length > 0) {
                    if (counter == Integer.parseInt(getId())) {
                        int total = Integer.valueOf(parts[7]) + 1;

                        String newLine = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3] + "-" + parts[4] + "-" + parts[5] + "-" + parts[6] + "-" + total;
                        fileContent.append(newLine);
                        fileContent.append("\n");
                    } else {
                        fileContent.append(strLine);
                        fileContent.append("\n");
                    }
                }
                counter++;
            }
            FileWriter fstreamWrite = new FileWriter("Buyer.txt");
            BufferedWriter out = new BufferedWriter(fstreamWrite);
            out.write(fileContent.toString());
            out.close();
            br.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
    }

}
