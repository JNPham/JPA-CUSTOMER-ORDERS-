/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2018 Alvaro Monge <alvaro.monge@csulb.edu>
 *
 */

package csulb.cecs323.app;

// Import all of the entity classes that we have written for this application.
import csulb.cecs323.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.Scanner;

/**
 * A simple application to demonstrate how to persist an object in JPA.
 * <p>
 * This is for demonstration and educational purposes only.
 * </p>
 * <p>
 *     Originally provided by Dr. Alvaro Monge of CSULB, and subsequently modified by Dave Brown.
 * </p>
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2021 David Brown <david.brown@csulb.edu>
 *
 */
public class CustomerOrders {
   /**
    * You will likely need the entityManager in a great many functions throughout your application.
    * Rather than make this a global variable, we will make it an instance variable within the CustomerOrders
    * class, and create an instance of CustomerOrders in the main.
    */
   private EntityManager entityManager;

   /**
    * The Logger can easily be configured to log to a file, rather than, or in addition to, the console.
    * We use it because it is easy to control how much or how little logging gets done without having to
    * go through the application and comment out/uncomment code and run the risk of introducing a bug.
    * Here also, we want to make sure that the one Logger instance is readily available throughout the
    * application, without resorting to creating a global variable.
    */
   private static final Logger LOGGER = Logger.getLogger(CustomerOrders.class.getName());

   /**
    * The constructor for the CustomerOrders class.  All that it does is stash the provided EntityManager
    * for use later in the application.
    * @param manager    The EntityManager that we will use.
    */
   public CustomerOrders(EntityManager manager) {
      this.entityManager = manager;
   }

   /** Checks if the inputted value is an integer and
	 * within the specified range (ex: 1-10)
	 * @param low lower bound of the range.
     * @param high upper bound of the range.
     * @return the valid input.
	 */
   public static int getIntRange( int low, int high ) {
      Scanner in = new Scanner( System.in );
      int input = 0;
      boolean valid = false;
      while( !valid ) {
         if( in.hasNextInt() ) {
            input = in.nextInt();
            if( input <= high && input >= low ) {
               valid = true;
            } else {
               System.out.println( "Invalid Range." );
            }
         } else {
            in.next(); //clear invalid string
            System.out.println( "Invalid Input." );
         }
      }
      return input;
   }

   public static void main(String[] args) {
      LOGGER.fine("Creating EntityManagerFactory and EntityManager");
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("CustomerOrders");
      EntityManager manager = factory.createEntityManager();
      // Create an instance of CustomerOrders and store our new EntityManager as an instance variable.
      CustomerOrders customerOrders = new CustomerOrders(manager);


      // Any changes to the database need to be done within a transaction.
      // See: https://en.wikibooks.org/wiki/Java_Persistence/Transactions

      LOGGER.fine("Begin of Transaction");
      EntityTransaction tx = manager.getTransaction();

      tx.begin();
      // List of Products that I want to persist.  I could just as easily done this with the seed-data.sql
      List <Products> products = new ArrayList<Products>();
      // Load up my List with the Entities that I want to persist.  Note, this does not put them
      // into the database.
      products.add(new Products("076174517163", "16 oz. hickory hammer", "Stanely Tools", "1", 9.97, 50));
      products.add(new Products("042187012933", "Mozzarella String Cheese", "American Heritage", "56", 15.99, 500));
      products.add(new Products("042187021355", "Mild Cheddar Chunk", "Best Yet", "12", 12.99, 400));
      products.add(new Products("016000435094", "Cheerios 12oz", "General Mills", "18", 6.60, 150));
      products.add(new Products("018894110675", "Toasted Oats", "Big Y", "39", 7.99, 90));
      products.add(new Products("045674530217", "Large Brown Eggs", "Star Market ", "30", 5.99, 800));
      products.add(new Products("688267049361", "Organic Extra Firm Tofu 14oz", "Natures Promise", "479", 3.50, 100));
      products.add(new Products("078742121703", "Crunchy Nugget", "Great Value", "53", 13.69, 500));
      products.add(new Products("042400070993", "Strawberry Cream Mini Spooners 18oz", "Malt-O-Meal", "95", 4.50, 100));
      products.add(new Products("036800661134", "Green Split Peas", "Food Club", "80", 2.00, 150));
      products.add(new Products("015400454780", "Crunchy Peanut Butter", "Carriage House", "269", 6.50, 250));
      // Create the list of products in the database.
      customerOrders.createEntity (products);

      // List of Customers that I want to persist.
      List <Customers> customers = new ArrayList<Customers>();
      customers.add(new Customers("Mcarthur", "Khaleesi", "Prospect Street", "90284", "484-645-8901"));
      customers.add(new Customers("Wooten", "Rivka", "Brown Avenue", "92840", "404-464-9377"));
      customers.add(new Customers("Riddle", "Samera", "Lumber Passage", "62589", "361-993-5588"));
      customers.add(new Customers("Draper", "Aysha", "Parkview Lane", "81462", "707-872-4957"));
      customers.add(new Customers("Mcmillan", "Inaaya", "Parkview Lane", "81462", "714-907-8655"));
      customers.add(new Customers("Truong", "Lewie", "Marble Passage", "49561", "701-527-7993"));
      customers.add(new Customers("Suarez", "Cody", "Lawn Route", "64521", "812-913-6880"));
      // Create the list of customers in the database.
      customerOrders.createEntity (customers);
      tx.commit();
      LOGGER.fine("End of Transaction");

      // Ask the users if they want to make an order.
      Scanner sc = new Scanner(System.in);
      System.out.println();
      System.out.println("--------------------Make Order---------------");
      System.out.println("Enter choice: ");
      System.out.println("1. Make an order.");
      System.out.println("2. Cancel.");
      int choice = sc.nextInt();
      //If yes, display list of available Customers and have them pick
      while (choice == 1) {
         System.out.println("Choose a Customer: ");
         for (int i = 0; i < customers.size(); i ++) {
            System.out.println((i+1) + ". " + customers.get(i).toString());
         }
         int cus = getIntRange(1,customers.size());
         // Ask users for the number of products and display the list of valid products and have them pick
         System.out.println("How many products? ");
         int quant = sc.nextInt();
         for (int j = 0; j<quant; j ++) {
            System.out.println("Choose a Product: ");
            for (int n = 0; n < products.size(); n ++) {
               System.out.println((n+1) + ". " + products.get(n));
            }
            int prod = getIntRange(1, products.size());
         }

         System.out.println("Enter choice: ");
         System.out.println("1. Make an order.");
         System.out.println("2. Cancel.");
         choice = sc.nextInt();
      }

      // Commit the changes so that the new data persists and is visible to other users.
      //tx.commit();
      //LOGGER.fine("End of Transaction");

   } // End of the main method

   /**
    * Create and persist a list of objects to the database.
    * @param entities   The list of entities to persist.  These can be any object that has been
    *                   properly annotated in JPA and marked as "persistable."  I specifically
    *                   used a Java generic so that I did not have to write this over and over.
    */
   public <E> void createEntity(List <E> entities) {
      for (E next : entities) {
         LOGGER.info("Persisting: " + next);
         // Use the CustomerOrders entityManager instance variable to get our EntityManager.
         this.entityManager.persist(next);
      }

      // The auto generated ID (if present) is not passed in to the constructor since JPA will
      // generate a value.  So the previous for loop will not show a value for the ID.  But
      // now that the Entity has been persisted, JPA has generated the ID and filled that in.
      for (E next : entities) {
         LOGGER.info("Persisted object after flush (non-null id): " + next);
      }
   } // End of createEntity member method

   /**
    * Think of this as a simple map from a String to an instance of Products that has the
    * same name, as the string that you pass in.
    * @param UPC        The name of the product that you are looking for.
    * @return           The Products instance corresponding to that UPC.
    */
   public Products getProduct (String UPC) {
      // Run the native query that we defined in the Products entity to find the right product.
      List<Products> products = this.entityManager.createNamedQuery("ReturnProduct",
              Products.class).setParameter(1, UPC).getResultList();
      if (products.size() == 0) {
         // Invalid style name passed in.
         return null;
      } else {
         // Return the style object that they asked for.
         return products.get(0);
      }
   }// End of the getProduct method
   
   /** A map from a String to an instance of Customer that has the
     * same id, as the string that you pass in. 
     * @param id        The id of the customer that you are looking for.
     * @return           The Customer instance corresponding to that id.
     */
   public Customers getCustomer (String id) {
      // Run the native query that we defined in the Customers entity to find the right customer.
      List<Customers> customers = this.entityManager.createNamedQuery("ReturnCustomer",
              Customers.class).setParameter(1, id).getResultList();
      if (customers.size() == 0) {
         // Invalid style name passed in.
         return null;
      } else {
         // Return the style object that they asked for.
         return customers.get(0);
      }
   }
} // End of CustomerOrders class
