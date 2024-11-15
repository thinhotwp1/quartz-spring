---

# 🚀 How to Implement This Project

### 1️⃣ Initialize Database

- 📂 Go to the `/database/` folder and select your preferred database: **MySQL**, **Oracle**, **H2**, or **PostgreSQL**.
- 📄 Copy and execute the database initialization script for your chosen database.

### 2️⃣ Configure Application Properties

- 🛠️ Open the configuration file located at `src/main/resources/application.properties`.
- 📝 Set up the data source properties as shown below:

    ```properties
    # Quartz configuration
    spring.quartz.properties.org.quartz.dataSource.blog.driver=com.mysql.cj.jdbc.Driver
    spring.quartz.properties.org.quartz.dataSource.blog.URL=jdbc:mysql://localhost:3306/blog?useSSL=false
    spring.quartz.properties.org.quartz.jobStore.dataSource=blog
    spring.quartz.properties.org.quartz.dataSource.blog.user=blog
    spring.quartz.properties.org.quartz.dataSource.blog.password=blog

    # MySQL configuration
    spring.datasource.url=jdbc:mysql://localhost:3306/blog?useSSL=false
    spring.datasource.username=blog
    spring.datasource.password=blog
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    ```

### 3️⃣ Run the Project

- ▶️ Start the project using your preferred method, for example, `mvn spring-boot:run` if you are using Maven.

--- 
