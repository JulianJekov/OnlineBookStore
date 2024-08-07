<div align="center">
  <h1>📚 Online Book Store 📚</h1>
  <p>Deployed: <a href="https://bookstore-bookstore.azuremicroservices.io/">HERE</a></p>
  <p>
    <a href="https://github.com/JulianJekov/OnlineBookStore/issues">Report Bug</a>
    ·
    <a href="https://github.com/JulianJekov/OnlineBookStore/issues">Request Feature</a>
  </p>
</div>
<details>
  <summary>📑 Table of Contents</summary>
  <ol>
    <li><a href="#-getting-started">Getting Started</a>
      <ul>
        <li><a href="#-prerequisites">Prerequisites</a></li>
        <li><a href="#-installation">Installation</a></li>
        <li><a href="#-setting-environment-variables">Setting Environment Variables</a></li>
      </ul>
    </li>
    <li><a href="#-about-the-project">About The Project</a>
      <ul>
        <li><a href="#-technologies">Technologies</a></li>
        <li><a href="#-features">Features</a></li>
      </ul>
    </li>
    <li><a href="#-usage">Usage</a></li>
  </ol>
  </ol>
</details>
## 🚀 Getting Started
<p>To get started with the Online Book Store project, follow these steps:</p>
📋 ### Prerequisites
<ul>
  <li>JDK 17+</li>
  <li>Apache Maven 3.6+</li>
  <li>MySQL</li>
</ul>
🛠 ### Installation
<p>To install and run the Online Book Store application:</p>
<ol>
  <li><a href="https://github.com/JulianJekov/OnlineBookStore/archive/refs/heads/master.zip">Download</a> the repository.</li>
  <li>Extract the downloaded zip file.</li>
  <li>Navigate to the project directory in your terminal.</li>
  <li>Run the following command to build and start the application:</li>
  <pre><code>mvn clean install</code></pre>
  <pre><code>mvn spring-boot:run</code></pre>
</ol>
🔧 ### Setting Environment Variables

<p>Before starting the application, set up the following environment variables ${MYYSQL_PORT}, ${MYSQL_USER}.. etc... in your application.properties or application.yml:</p>

```yaml
    datasource:
        driver-class-name: com.mysql.cj.jdbc.Driver
        url: jdbc:mysql://${MYSQL_HOST}:${MYSQL_PORT}/book-store?useSSL=false&createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true
        password: ${DB_PASSWORD}
        username: ${DB_USERNAME}
```
<p align="right">(<a href="#readme-top">back to top</a>)</p>
📖 ### About The Project
<p>The Online Book Store project is a Spring Boot MVC application designed to manage book catalogs, user profiles, and more.</p>
🛠 ### Technologies
<p align="center">
  <img src="https://img.shields.io/badge/Java-ED4236?logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=spring-boot&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?logo=spring&logoColor=white">
  <img src="https://img.shields.io/badge/Spring%20Security-6DB33F?logo=spring-security&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/Bootstrap-563D7C?logo=bootstrap&logoColor=white">
  <img src="https://img.shields.io/badge/HTML5-E34F26?logo=html5&logoColor=white">
  <img src="https://img.shields.io/badge/CSS3-1572B6?logo=css3&logoColor=white">
</p>
🌟 Features
<ul>
  <li><strong>User Management:</strong>
    <ul>
      <li>Users can register and log in.</li>
      <li>Email verification is required for account activation.</li>
      <li>Users can edit their profiles and change passwords.</li>
    </ul>
  </li>
  <li><strong>Book Management:</strong>
    <ul>
      <li>Users can view all books and add books to their shopping cart.</li>
      <li>Users can write, edit, and delete their own reviews.</li>
      <li>Admins can add, edit, and delete books and reviews.</li>
    </ul>
  </li>
  <li><strong>Shopping Cart:</strong>
    <ul>
      <li>Users can add books to their cart, remove them, and proceed to checkout.</li>
      <li>Order history is maintained for users to review past purchases.</li>
    </ul>
  </li>
  <li><strong>Internationalization:</strong>
    <ul>
      <li>Supports both Bulgarian and English languages.</li>
    </ul>
  </li>
  <li><strong>Email Notifications:</strong>
    <ul>
      <li>Send emails for account validation and for inactive account warnings.</li>
    </ul>
  </li>
  <li><strong>Security:</strong>
    <ul>
      <li>OAuth authentication for Google and GitHub login.</li>
      <li>Custom exceptions for specific error handling needs.</li>
    </ul>
  </li>
  <li><strong>Administration:</strong>
    <ul>
      <li>Admins can manage users, books, and reviews.</li>
    </ul>
  </li>
  <li><strong>Testing:</strong>
    <ul>
      <li>Uses JUnit 5, Mockito, GreenMail, and HSQLDB for unit and integration tests.</li>
    </ul>
  </li>
</ul>
<p align="right">(<a href="#readme-top">back to top</a>)</p>
💻 Usage
<p>To use the Online Book Store application:</p>
<ul>
  <li>Register and activate your account via the email verification link.</li>
  <li>Log in using your credentials or via Google/GitHub OAuth.</li>
  <li>Browse the book catalog, add books to your shopping cart, and proceed to checkout.</li>
  <li>Review your order history and manage your profile settings.</li>
  <li>Admins have additional functionalities to manage books, users, and reviews.</li>
</ul>
<p align="right">(<a href="#readme-top">back to top</a>)</p>
