<img src="https://webassets.telerikacademy.com/images/default-source/logos/telerik-academy.svg)" alt="logo" width="300px" style="margin-top: 20px;"/>

# Virtual Wallet

### A project by Team 1 Dian Gospodinov & Bojidar Velichkov

Transfer money straight from your debit/credit card into your virtual wallet and make 
quick and easy transactions to your friends!

### How to take make use of our application?

- Register with just an email, username and password.
- You will receive a verification email, to prove that it is valid.
- Follow the link to activate your account.
- You are now able to make transactions!

### Features of the program

- If you ever encounter a problem, you will receive a proper 
<span style="color: #36b9cc">notification</span>
or <span style="color: #be2617">error</span> message to assist you.

- If you would like to make  a **transaction** for over $1000, the application will 
send a **verification code** to your email that you must enter in the corresponding 
field to complete the **transaction**.

- You can upload your own **profile photo**, and it will save it on the server, so 
you don't have to keep track of it.

- Whenever your **card** expires, you can 
<kbd style="background-color: #1cc88a; color: #fff; border: 1px">Add</kbd> a new one, 
<kbd style="background-color: #e74a3b; color: #fff; border: 1px">Delete</kbd> or 
<kbd style="background-color: #4e73df; color: #fff; border: 1px">Edit</kbd> the old one.

- Sometimes a bank **transfer** to deposit money to your **wallet** does not go as planned. 
Do not worry about it, as these things happen, you can simply try again, as long
as your card is valid.

- You are enjoying our application so much that you have made so many transactions
that it is getting hard to find a specific one? You can make use of our filter 
system and sort them by <kbd>Amount</kbd> or <kbd>Date</kbd> just by clicking on the
corresponding column header.

- If you have climbed the ranks to be an **admin** of our app, you have your
own <kbd>Admin Menu</kbd>, where you can search through all users and transactions and
<kbd style="background-color: #e74a3b; color: #fff; border: 1px">Block</kbd> or
<kbd style="background-color: #4e73df; color: #fff; border: 1px">Unblock</kbd>a user 
if you notice suspicious activity.

### Authentication

**Public part** 

Anonymous users can only register or login to the application, as there is some very sensitive
information that they must not be able to access. Upon registration, they will receive 
a confirmation email to verify their account.

**Private part**

- **Users** who have logged in successfully can make **transfers** from their bank 
to their **Virtual Wallet**,
send money to other **users** by searching for their **username**, **email** or **phone number**. 
If they make a **transaction** over $1000, they must 
<span style="color: #36b9cc">verify</span> it by entering the **code** sent to their email.

- They can check their activity history by going to the <kbd>Activity Log</kbd> page,
<kbd style="background-color: #4e73df; color: #fff; border: 1px">Filter</kbd>
it by <kbd>Date</kbd> and <kbd>Direction</kbd>.

- **Users** can view and <kbd style="background-color: #4e73df; color: #fff; border: 1px">Edit</kbd>
their profile information, except their **username**, which is selected upon registration and 
can not be changed afterwards.

- Each **user** can add one or more debit/credit **card**, which is used to **transfer**
money into their **Virtual Wallet**.

- **Users** must be able to <kbd style="background-color: #4e73df; color: #fff; border: 1px">Send</kbd>
money to other **users** by entering another user's **phone number**,
**username** or **email** and desired **amount** to be sent,
but when viewing recipient **users** only the **username** is displayed.

>_Note: The transfer is done by a separate dummy REST API. It confirms transfers on random basis_

- Each **transaction** goes through a confirmation step, which displays the **transaction** details
and allows either confirming or canceling it.

**Administrative part**

**Admin users** can see a list of all **users** and
<kbd style="background-color: #4e73df; color: #fff; border: 1px">Search</kbd> them by **phone number**,
**username** or **email** and <kbd style="background-color: #e74a3b; color: #fff; border: 1px">Block</kbd> /
<kbd style="background-color: #4e73df; color: #fff; border: 1px">Unblock</kbd>
or <kbd style="background-color: #1cc88a; color: #fff; border: 1px" >Promote</kbd> them.
A <span style="color: #e74a3b">blocked</span> **user** must be able to do everything as normal **user**,
except to make **transactions**.

**Admin users** must be able to view a list of all **transactions** and can
<kbd style="background-color: #4e73df; color: #fff; border: 1px">Filter</kbd> it by **period**,
**sender**, **recipient** and **sort** them by <kbd>Amount</kbd> or <kbd>Date</kbd>.

### Database Visualization

<img src="https://gitlab.com/team-123/virtual-wallet/-/raw/main/database/Database%20Visualization.png">

### Swagger Documentation

<a href="http://localhost:8080/swagger-ui/">http://localhost:8080/swagger-ui/ </a> 
