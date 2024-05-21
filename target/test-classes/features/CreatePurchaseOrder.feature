Feature: Verify the functionality of Purchase Order API

  Background: Generate access token from username and password
    Given Give Username and Password to get Access Token
      | username  | integrator   |
      | password  | 3poXy$6o29   |
      | requestId | 5001597022-1 |

  @CreatePurchaseOrder
  Scenario: Verify that user is able to create new purchase order from Purchase Order API
    And Get the PO number to create a new purchase order
    And Company code for the purchase order is "H"
    And Document type for the purchase order is "Normal"
    And Get the po date for the purchase order
    And Supplier for the purchase order is "H0022"
    And Buyer for the purchase order is ""
    And Provide the Items details for which purchase order has to be created
      | item | ean           | material  | plnt | poDueDate | shortText                           | matlGroup | delIndicator | quantity | leQuantity | mrp | caselot | weight | volume | isBlocked | isDeleted |
      |      | 8900001315524 | 500002235 | 1039 |           | PONDS WHITE BTY BB FARNS CREAM(18G) | Food      | 0            | 100      | 10         | 160 | 5       | 1      | 0.01   | true      | true      |
      |      | 8900001315536 | 500003990 | 1039 |           | PONDS AGE MIRACLE(30G)              | Food      | 0            | 300      | 10         | 160 | 15      | 1      | 0.001  | true      | true      |
      |      | 8900001315537 | 500019306 | 1039 |           | VIM DROP 2XLEMON GEL DISHWASH(1.5L  | Food      | 0            | 300      | 10         | 160 | 5       | 1      | 0.01   | true      | true      |
      |      | 8900001315538 | 500018493 | 1039 |           | LIFEBUOY NATURE HANDWA REFI(750ML)  | Food      | 0            | 300      | 10         | 160 | 20      | 1      | 0.01   | true      | true      |
      |      | 8900001315539 | 500003055 | 1039 |           | LAKME SUN EXPT SPF 30 ALSKIN(50ML)  | Cosmetics | 0            | 300      | 10         | 160 | 5       | 1      | 0.01   | true      | true      |
      |      | 8900001315540 | 500012556 | 1039 |           | DOVE NUT OXYGEN MOISTU SHAMPO-180M  | Cosmetics | 0            | 300      | 10         | 160 | 15      | 1      | 0.01   | true      | true      |
      |      | 8900001315541 | 500019389 | 1039 |           | DOVE DRYNESS CARE SHAMPOO(340ML)    | Cosmetics | 0            | 300      | 10         | 160 | 15      | 2      | 0.11   | true      | true      |
      |      | 8900001315542 | 500002867 | 1039 |           | DOVE REFRESHING BODY WASH(250ML)    | Cosmetics | 0            | 300      | 10         | 160 | 15      | 1      | 0.21   | true      | true      |
      |      | 8900001315543 | 500007451 | 1039 |           | LAKME BLU N GLO POMGR SHET MASK(1U) | Cosmetics | 0            | 300      | 10         | 160 | 15      | 1      | 0.11   | true      | true      |
    When Requester calls the purchase order api endpoint to create a new purchase order
    Then Verify that status code be equal to 201

  @Excel
  Scenario: Verify that user is able to create new purchase order from Purchase Order API
    And Get the PO number to create a new purchase order
    And Company code for the purchase order is "H"
    And Document type for the purchase order is "Normal"
    And Get the po date for the purchase order
    And Supplier for the purchase order is "H0022"
    And Buyer for the purchase order is ""
    And And Give the Excel file to get the items "Purchase Order Items.xlsx"
    And Give the sheet name to get the items for which purchase order has to be created "Items"
    When Requester calls the purchase order api endpoint to create a new purchase order
    Then Verify that status code be equal to 201

  @Excel
  Scenario: Verify that user is able to create new purchase order from Purchase Order API
    And Get the PO number to create a new purchase order
    And Company code for the purchase order is "H"
    And Document type for the purchase order is "Normal"
    And Get the po date for the purchase order
    And Supplier for the purchase order is "H0022"
    And Buyer for the purchase order is ""
    And And Give the Excel file to get the items "Purchase Order Items.xlsx"
    And Give the sheet name "Items" and the number of items required to create purchase order 3
    When Requester calls the purchase order api endpoint to create a new purchase order
    Then Verify that status code be equal to 201
