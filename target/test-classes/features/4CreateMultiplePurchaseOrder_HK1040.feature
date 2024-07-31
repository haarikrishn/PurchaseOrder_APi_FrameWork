Feature: Create a multiple purchase orders


  @Authorization
  Scenario: generate bearer token for vendor appointmet
    When  pass precheck condition for client id
      | client_id                            | nonce                                | state                                | user_name                        | response_type | redirect_url                        | scope  |
      | b4204b8f-81ae-4d32-8724-06e4368123a1 | 76ed27b3-015e-4264-8fa1-b93d7867c163 | 40f6125c-a1b9-4795-a564-161bcf896d32 | harikrishna.medari@qualitrix.com | code          | https://canary.dmartlink.com/app/po | openid |
    And Generate otp for particular client id
      | otp    | nonce                                |
      | 111111 | 3691c15c-51de-4be7-91f5-0221a4b73c49 |
    And enter pin to access the application
      | nonce                                | pin  |
      | 3691c15c-51de-4be7-91f5-0221a4b73c49 | 1234 |
    And enter the Auth code to access the application
      | nonce                                |
      | 6507049f-2d69-491c-98ba-31f064849b69 |
    And pass otp and generate bearer token
      | code                                 | grant_type         |
      | a0d3996c-9a19-451e-bce7-0277f8b2068e | authorization_code |


  @CreateMultiplePosUsingExcel2
  Scenario Outline: Create a multiple  purchase orders using Excel
    Given Give Username and Password to get Access Token
      | username | integrator |
      | password | 3poXy$6o29 |
    And creating purchase order with different suppliercode "<supplierCode>"
    When user pass to  main mandatory fields to create a multiple purchase orders
      | companyCode | documentType | buyerCode | buyerEmail                     | tdLine                                         | tdFormat | tdLine1                                         | tdFormat1 |
      | ASPL        | ZANB         | H10       | phiroze.lakhani@dmartindia.com | line 1: This is line one of terms of delivery. | *        | line 2: This the line tow of terms of delivery. | *         |

    And user add to  list of articles  "<excelFile>" "<SheetName>" "<requiredTasks>" "<siteId>"to create a multiple purchase orders
    And user send multiple requests to server using create Po APi
    Then Verify that status code be equal to 201
    Examples:
      | siteId | excelFile                      | SheetName    | requiredTasks | supplierCode |
#     | 1023   | dlinkpostdata (2).xlsx | PO_Items_1023 | 3             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 301           | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 301           | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 301           | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 301           | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 301           | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 2             | 100245       |
#
#
  @UpdateMultiplePosUsingExcel
  Scenario Outline: Update a multiple  purchase orders using Excel
    Given Give Username and Password to get Access Token
      | username | integrator |
      | password | 3poXy$6o29 |
    And creating purchase order with different suppliercode "<supplierCode>" to updatepo
    When user pass to  main mandatory fields to update a multiple purchase orders for updatepo
      | companyCode | documentType | buyerCode | buyerEmail                     | tdLine                                         | tdFormat | tdLine1                                         | tdFormat1 |
      | ASPL        | ZANB         | H10       | phiroze.lakhani@dmartindia.com | line 1: This is line one of terms of delivery. | *        | line 2: This the line tow of terms of delivery. | *         |

    And user add to  list of articles  "<excelFile>" "<SheetName>" "<requiredTasks>" "<siteId>"to update a multiple purchase orders
    And user send multiple requests to server using Update Po APi
    Then Verify that status code be equal to 201
    Examples:
      | siteId | excelFile                      | SheetName | requiredTasks | supplierCode |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 50            | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |
#      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 2             | 100245       |


  @GetPO
  Scenario: Get all the po's which are created in DC
    And pass the po number as a Query parameter
    When  user call GetAllPO Api user should get all pos in DC
    Then Verify that status code be equal to 200

#  @GetSlots
#  Scenario: Get slots from particular DC
#   When whenever vendor created purchase order,Dc should give availability time
##And send bulk amount of request and get the slots
#    Then  Verify that status code be equal to 200
#
  @GetslotsConfirmAppointmentAndCreateGRN
  Scenario: Get slots and confirm appontement
#    When whenever vendor created purchase order,Dc should give availability time,and confirm appointment
#      | truckType | truckNumber | driverName | driverNumber | dstSiteId |
#      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
#      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
    And whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN
      | truckType | truckNumber | driverName | driverNumber | dstSiteId |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
#      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
#      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
#      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
#      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
#      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |

    Then  Verify that status code be equal to 200




















#  @ConfirmAppointment
#
#  Scenario: confirm appointment for particular DC
##  When whenever vendor created purchase order,Dc should give availability time,and confirm appointment status
##    | truckType | truckNumber | driverName | driverNumber | dstSiteId |
##    | 1109      | KA12AA2653  | krishna    | 987654321    | 1023      |
#    And set availabilty time based on DC
#      | truckType | truckNumber | driverName | driverNumber | dstSiteId |
#      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
#      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
#      | 1109      | KA12AA2655  | krishna    | 987654323    | 1033      |
#      | 1109      | KA12AA2656  | hari       | 987654324    | 1033      |
#      | 1109      | KA12AA2657  | krishna    | 987654325    | 1033      |
##      | 1109      | KA12AA2658  | hari       | 987654326    | 1033      |
##      | 1109      | KA12AA2659  | krishna    | 987654327    | 1033      |
##      | 1109      | KA12AA2660  | hari       | 987654328    | 1033      |
##      | 1109      | KA12AA2661  | krishna    | 987654329    | 1033      |
##      | 1109      | KA12AA2662  | hari       | 987654320    | 1033      |
##      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      |
##      | 1109      | KA12AA2654  | hari       | 987654322    | 1033      |
##      | 1109      | KA12AA2655  | krishna    | 987654323    | 1033      |
##      | 1109      | KA12AA2656  | hari       | 987654324    | 1033      |
##      | 1109      | KA12AA2657  | krishna    | 987654325    | 1033      |
##      | 1109      | KA12AA2658  | hari       | 987654326    | 1033      |
##      | 1109      | KA12AA2659  | krishna    | 987654327    | 1033      |
##      | 1109      | KA12AA2660  | hari       | 987654328    | 1033      |
##      | 1109      | KA12AA2661  | krishna    | 987654329    | 1033      |
##      | 1109      | KA12AA2662  | hari       | 987654320    | 1033      |
##    And send request to server using ConfirmAppointment Api
##    Then  Verify that status code be equal to 200
##
##  @ViewAppointment
##
##  Scenario: view appointment for particular DC
##
##    Given Give Username and Password to get Access Token
##      | username | 444106    |
##      | password | Dmart@123 |
##    When whenever vendor created purchase order,Dc should give availability time,and confirm appointment status and after view the appontment
##      | columnKey      | orderBy | dstSiteId |
##      | PoCreationDate | DESC    | 1033       |
##
##    And send reuest to server using view Appontment Api
##    And  verify the asnNumber appointmentId  and poNumber
##    Then  Verify that status code be equal to 200
#
##  @GetPO
##  Scenario: Get all the po's which are created in DC
##    And pass the po number as a Query parameter
##    When  user call GetAllPO Api user should get all pos in DC
##
##    Then Verify that status code be equal to 200
##
##    @CreateGRN
##    Scenario: create Grn for particular Po number
##      Given Give Username and Password to get Access Token
##        | username | integrator |
##        | password | 3poXy$6o29 |
## When  after appontment scheduled create Grn for particular po number
##   | postingDate | grnDate | invoiceNumber | inwardNumber | userId | headerText | grnNumber |
##   |             |         |               | 5242688      | Hari   |            |           |
###      And Send Grn request to the server using api
##      Then  Verify that status code be equal to 201
