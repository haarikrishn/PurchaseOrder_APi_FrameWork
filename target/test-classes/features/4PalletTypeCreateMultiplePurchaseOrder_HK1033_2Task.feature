Feature: Create a multiple purchase orders


  @Authorization_2
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
    And save the  bearer token

    Then Verify that status code be equal to 200

#    ==============================================================================================================================================================================
#  with pallet


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
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | PO_Items1023 | 10             | 100245       |

  @UpdateMultiplePosUsingExcel3
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
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10             | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10            | 100245       |
      | 1033   | PO Automation Payload (3).xlsx | UpdatePO  | 10             | 100245       |



  @GetPO
  Scenario: Get all the po's which are created in DC
    When  user call GetAllPO Api user should get all pos in DC
    Then Verify that status code be equal to 200
    And validate schema for "po.json" file

  @GetCalender
  Scenario: get the calender about particular DC
    When user call the get calender api
    Then Verify that status code be equal to 200

  @GetslotsConfirmAppointmentAndCreateGRN
  Scenario: Get slots and confirm appontement and create GRN
    When  call all the methods to execute the script
      | isPalletized |
      | true         |
      | true        |
    And using calender date whenever vendor created purchase order,Dc should give availability time,and confirm appointment and create a GRN and Validate
      | truckType | truckNumber | driverName | driverNumber | dstSiteId | isPalletized |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
      | 1109      | KA12AA2653  | krishna    | 987654321    | 1033      | true        |
    Then Verify that status code be equal to 200