# Module-01-Coding-Standards

## Refleksi 1

---

### Prinsip Clean Code yang Diterapkan

1. **Separation of Concerns**, saya menerapkannya dengan memisahkan controller, service, dan repository ke dalam package yang berbeda. Pemisahan ini membuat setiap lapisan memiliki tanggung jawab yang jelas dan mencegah pencampuran logika antar layer, sehingga kode lebih mudah dipahami dan dimaintain.

2. **Single Responsibility Principle**, saya memastikan setiap kelas hanya memiliki satu tanggung jawab utama. Controller hanya bertugas menangani request dan response HTTP, service menangani logika bisnis, dan repository menangani penyimpanan serta pengambilan data.

3. **Meaningful Naming**, saya melakukan penamaan kelas, metode, dan variabel secara deskriptif seperti `ProductController`, `ProductService`, `create`, `findAll`, `findById`, `update`, dan `deleteById`. Penamaan ini memungkinkan tujuan dan fungsi kode dapat dipahami tanpa perlu membaca implementasinya secara mendetail.

4. **Don't Repeat Yourself (DRY)**, saya menggunakan Lombok untuk mengurangi boilerplate code seperti getter dan setter. Template Thymeleaf juga memanfaatkan fragment dan reusable components untuk menghindari duplikasi markup HTML.

5. **Consistent Formatting**, saya menerapkan format kode yang konsisten dengan indentasi seragam, penempatan kurung kurawal yang konsisten, dan organisasi import statement yang teratur untuk meningkatkan readability.

---

### Praktik Secure Coding yang Diterapkan

1. **Avoiding SQL Injection Risks**, pada tahap ini repository masih menggunakan in-memory storage sehingga tidak ada interaksi langsung dengan database. Ketika nantinya menggunakan database, akan diterapkan prepared statement atau ORM untuk mencegah SQL injection.

2. **UUID untuk ID Produk**, penggunaan UUID untuk generate ID produk secara otomatis mencegah prediktabilitas ID dan mengurangi risiko serangan enumeration attack dimana attacker mencoba menebak ID produk secara berurutan.

3. **Post/Redirect/Get Pattern**, saya menerapkan pattern ini setelah operasi yang mengubah data seperti create, update, dan delete. Dengan melakukan redirect setelah POST, aplikasi dapat mencegah pengiriman ulang data yang sama ketika pengguna melakukan refresh halaman.

4. **Minimalisir Data Exposure**, view template hanya menampilkan data yang benar-benar dibutuhkan oleh user interface. Hal ini membantu mengurangi risiko kebocoran data sensitif yang tidak perlu.

---

### Kesalahan yang Ditemukan dan Cara Perbaikan

1. **Penghapusan data menggunakan metode GET tidak aman** karena dapat memicu penghapusan tidak disengaja melalui prefetch, web crawler, atau disalahgunakan melalui link berbahaya. Perbaikan yang disarankan adalah menggunakan metode POST atau DELETE dengan perlindungan CSRF melalui Spring Security dan menggunakan form submission untuk delete action.

2. **Validasi input belum diterapkan** pada field seperti `productName` dan `productQuantity`. Field name bisa menerima string kosong atau terlalu panjang, sedangkan quantity bisa menerima nilai negatif. Perbaikan dapat dilakukan dengan menambahkan anotasi validasi Bean Validation seperti `@NotBlank`, `@Size`, dan `@Min` pada model, serta menangani validation error di controller untuk menampilkan pesan error yang informatif.

3. **Penanganan error masih minim** karena ketika data produk tidak ditemukan, aplikasi hanya melakukan redirect tanpa memberikan informasi kepada pengguna. Perbaikannya adalah dengan mengubah return type method menjadi `Optional`, melakukan pengecekan keberadaan data, dan menampilkan pesan error atau halaman khusus agar pengguna memahami apa yang terjadi.

4. **Manajemen ID produk masih lemah** karena ID dihasilkan di repository dengan pengecekan sederhana. Jika ID diberikan secara manual tanpa validasi ketat, bisa terjadi duplikasi atau inkonsistensi. Perbaikannya adalah dengan selalu menghasilkan ID di sisi server menggunakan UUID dan tidak mengizinkan client untuk menentukan ID.

5. **Tipe input quantity kurang tepat** karena masih menggunakan `type="text"` yang memungkinkan input karakter non-numerik. Perbaikan dapat dilakukan dengan mengganti menjadi `type="number"` dengan atribut `min` untuk validasi client-side, namun tetap menambahkan validasi server-side karena client-side validation dapat di-bypass.

---

## Refleksi 2

---

### Unit Test, Code Coverage, dan Kualitas

Setelah menulis unit test, saya merasa lebih percaya diri karena setiap fungsi sudah terdokumentasi dan dapat diverifikasi secara otomatis. Unit test juga berfungsi sebagai safety net ketika melakukan refactoring atau menambah fitur baru, sehingga dapat segera mendeteksi jika ada fungsionalitas yang rusak.

Jumlah unit test dalam sebuah kelas sebaiknya disesuaikan dengan **kompleksitas fungsi dan logika** yang perlu diverifikasi. Prinsip yang saya gunakan adalah **satu test untuk satu skenario**, yang memungkinkan semua edge case dapat terverifikasi dengan baik. Selain itu, fokus pada testing public methods dan behavior, bukan detail implementasi internal.

Untuk class repository misalnya, minimal harus ada test untuk:
- Method `create()`: skenario produk baru, produk dengan ID existing, produk dengan ID null
- Method `findAll()`: list kosong, list dengan beberapa produk
- Method `findById()`: ID valid, ID tidak ditemukan, ID null
- Method `update()`: produk existing, produk tidak ditemukan
- Method `deleteById()`: ID valid, ID tidak ditemukan

Untuk memastikan test sudah cukup, saya mengombinasikan beberapa pendekatan:

1. **Memastikan semua public method dan kondisi penting sudah diuji** dengan fokus pada happy path, edge cases, dan error scenarios.

2. **Menggunakan Code Coverage sebagai panduan**, metrik ini mengukur persentase kode yang dieksekusi saat test berjalan. Ada beberapa jenis coverage: line coverage (persentase baris yang dieksekusi), branch coverage (persentase cabang kondisi yang dieksekusi), method coverage, dan class coverage. Tools seperti JaCoCo, Cobertura, atau IntelliJ IDEA built-in coverage dapat digunakan untuk mengukur coverage. Target yang realistis adalah 80-90% untuk production code.

3. **Menerapkan Mutation Testing** seperti PIT/PITest untuk mengecek kualitas test dengan mengubah kode dan melihat apakah test mendeteksi perubahan tersebut.

**Apakah 100% coverage berarti bebas bug?** Jawabannya adalah **TIDAK**. Alasannya:

- **Coverage tidak sama dengan correctness**. Test bisa mengeksekusi semua baris kode tanpa melakukan assertion yang kuat atau tepat. Misalnya test yang hanya memanggil method tanpa memvalidasi hasilnya.

- **Tidak menguji semua kombinasi input**. Impossible untuk test semua kemungkinan input, sehingga edge cases atau corner cases tertentu bisa terlewat.

- **Tidak mendeteksi logic error**. Bug bisa tersembunyi pada logika bisnis yang kompleks, atau test mungkin mengecek hasil yang salah sehingga bug tetap lolos meskipun test pass.

- **Tidak menguji integration issues**. Unit test mengisolasi class sehingga tidak menguji interaksi antar komponen. Bug bisa muncul ketika komponen digabungkan.

- **Tidak menguji concurrency issues** seperti race conditions atau deadlocks yang hanya muncul pada kondisi multi-threading.

- **Tidak menguji runtime issues** seperti memory leaks, performance problems, atau timeout.

Kesimpulannya, code coverage adalah **alat bantu**, bukan **tujuan akhir**. Yang lebih penting adalah **quality of tests** daripada quantity of coverage. Test harus mencakup berbagai skenario dan dikombinasikan dengan integration tests, functional tests, dan manual testing.

---

### Refleksi Clean Code pada Suite Uji Fungsional Baru

Jika saya membuat suite uji fungsional baru yang sangat mirip dengan `CreateProductFunctionalTest`, kemudian menyalin prosedur setup dan variabel instance yang sama, maka **code quality akan menurun** karena beberapa alasan:

**Clean Code Issues yang Teridentifikasi:**

1. **Duplicate Code (Violation of DRY Principle)**
   - Codingan setup yang sama (annotations, fields, method setup) muncul berulang di banyak test class
   - Perubahan kecil pada setup membutuhkan edit di banyak tempat yang berbeda
   - Maintenance overhead meningkat signifikan seiring bertambahnya jumlah test class

2. **Inisialisasi Berulang**
   - Variabel instance seperti `serverPort`, `testBaseUrl`, dan `baseUrl` dideklarasikan identik di setiap class
   - Method `setupTest()` dengan anotasi `@BeforeEach` ditulis persis sama berkali-kali
   - Annotations seperti `@SpringBootTest` dan `@ExtendWith` diulang di setiap class
   - Risiko inkonsistensi meningkat jika ada yang lupa mengupdate salah satu class

3. **Lack of Abstraction**
   - Tidak ada base class atau shared setup yang mengenkapsulasi common functionality
   - Setiap test class harus mengurus setup sendiri meskipun kebutuhannya identik
   - Helper methods tidak ter-centralized sehingga juga harus diduplikasi

4. **Reduced Maintainability**
   - Sulit untuk maintain karena perubahan konfigurasi test harus direplikasi ke semua test class
   - Error-prone karena mudah lupa update salah satu class saat ada perubahan
   - Testing overhead tinggi: lebih banyak boilerplate daripada actual test logic
   - Developer baru akan kesulitan memahami pola testing karena terlalu banyak repetisi

**Dampak terhadap Code Quality:**

Ya, **code duplication akan mengurangi kualitas kode** karena:
- **Maintainability menurun**: lebih sulit untuk maintain dan update
- **Readability menurun**: developer harus membaca banyak boilerplate yang sama berulang kali
- **Error-prone**: tingginya risiko inconsistency antar test classes
- **Violates SOLID Principles**: khususnya prinsip DRY dan Single Responsibility

**Perbaikan yang Dapat Dilakukan:**

1. **Membuat Base Test Class**
   - Buat abstract class misalnya `BaseFunctionalTest` yang berisi semua common setup
   - Pindahkan annotations, instance variables, dan method `setupTest()` ke base class
   - Deklarasikan instance variables sebagai `protected` agar accessible dari subclass
   - Tambahkan helper methods yang reusable seperti `navigateToProductList()`, `navigateToCreateProduct()`, dan `createProduct()`
   - Setiap test class cukup extends `BaseFunctionalTest` tanpa perlu menulis ulang setup code

2. **Menggunakan Page Object Pattern**
   - Buat class terpisah untuk setiap halaman web (contoh: `ProductListPage`, `CreateProductPage`)
   - Page Object mengenkapsulasi element locators dan interactions untuk halaman tersebut
   - Test class tidak langsung berinteraksi dengan Selenium API, tapi melalui Page Objects
   - Keuntungan: abstraksi yang baik, reusability tinggi, maintainability meningkat, dan readability lebih baik

3. **Menggunakan @BeforeEach atau @BeforeAll secara konsisten**
   - Manfaatkan lifecycle hooks di base class untuk setup yang konsisten
   - Hindari duplikasi setup logic di setiap test method

4. **Membuat Helper atau Test Utility Class**
   - Centralisasi common operations seperti create test data, navigate, atau assertions
   - Misalnya `createTestProducts(count)` untuk membuat sejumlah produk test sekaligus
   - `countProductsInList()` untuk menghitung jumlah produk di list

5. **Test Parameterization**
   - Jika skenario test berbeda hanya pada input dan expected output, gunakan parameterized test
   - Mengurangi jumlah test methods yang repetitive

**Keuntungan dari Refactoring:**

- **DRY Compliance**: tidak ada code duplication
- **Better Maintainability**: perubahan setup hanya di satu tempat
- **Improved Readability**: test lebih fokus pada behavior, bukan boilerplate
- **Reusability**: helper methods dan page objects bisa digunakan ulang
- **Consistency**: setup yang seragam di semua test
- **Easier Testing**: lebih mudah dan cepat menulis test baru

---
# Module-02-CI/CD-DevOps

## Refleksi 

### Masalah Kualitas Kode yang Diperbaiki dan Strategi Perbaikannya

Selama pengerjaan modul 2, saya mengidentifikasi dan memperbaiki beberapa masalah kualitas kode:

1. **Masalah PMD Violations**
   - **Issue**: Terdapat 13 violation dari PMD ruleset yang dilaporkan oleh static code analyzer
   - **Perbaikan**: Menyesuaikan konfigurasi PMD ruleset.xml untuk mengabaikan violation yang tidak relevan dengan proyek Spring Boot seperti:
     - Aturan `UnusedPrivateMethod` untuk method `main()` yang memang tidak dipanggil secara langsung
     - Aturan `TooManyStaticImports` karena penggunaan static import pada Spring Boot test annotations adalah praktik umum
     - Aturan `JUnitTestsShouldIncludeAssert` karena test menggunakan Selenium assertions
     - Aturan `JUnitAssertionsShouldIncludeMessage` karena tidak diperlukan untuk test fungsional
   - **Strategi**: Fokus pada violation yang benar-benar mempengaruhi kualitas kode dan mengabaikan false positives dari tool

2. **Masalah Code Duplication pada Test Class**
   - **Issue**: Setup code yang identik berulang di `CreateProductFunctionalTest` dan `HomePageFunctionalTest`
   - **Perbaikan**: Membuat `BaseFunctionalTest` abstract class yang mengandung common setup
   - **Strategi**: Menerapkan prinsip DRY dengan mengenkapsulasi shared functionality ke base class

3. **Masalah Test Naming Convention**
   - **Issue**: Nama test method tidak konsisten dan tidak mengikuti format Given-When-Then
   - **Perbaikan**: Menstandarisasi nama test menjadi format yang deskriptif seperti `testCreateProduct_WhenValidData_ThenProductIsCreated`
   - **Strategi**: Menggunakan naming convention yang jelas untuk meningkatkan readability dan dokumentasi otomatis

4. **Masalah Test Data Management**
   - **Issue**: Hard-coded test data di dalam test methods
   - **Perbaikan**: Membuat helper methods seperti `createTestProduct()` untuk menghasilkan test data yang konsisten
   - **Strategi**: Centralisasi test data creation untuk memudahkan maintenance dan mengurangi duplication

---

### Evaluasi CI/CD Implementation

Pipeline CI/CD yang saya implementasikan **sudah memenuhi definisi Continuous Integration dan Continuous Deployment** dengan alasan sebagai berikut:

**Continuous Integration Aspects:**
1. **Automated Testing**: Setiap commit memicu eksekusi otomatis unit test, functional test, dan PMD code analysis. Ini memastikan bahwa setiap perubahan kode diverifikasi secara menyeluruh sebelum integrasi ke codebase utama.

2. **Immediate Feedback**: Developer mendapatkan feedback instan melalui GitHub Actions workflow status dan test reports. Jika ada test yang gagal atau violation ditemukan, developer langsung tahu dan dapat memperbaikinya sebelum kode di-merge.

3. **Integration Validation**: Pipeline memverifikasi bahwa semua komponen aplikasi dapat bekerja sama dengan baik melalui end-to-end testing menggunakan Selenium, bukan hanya unit testing terisolasi.

**Continuous Deployment Aspects:**
1. **Automated Deployment**: Setelah semua test dan quality gates lulus, aplikasi secara otomatis dideploy ke PaaS (Render) tanpa intervensi manual, memenuhi prinsip deployment automation.

2. **Environment Consistency**: Menggunakan Docker container memastikan bahwa environment development, testing, dan production konsisten, mengurangi kemungkinan "works on my machine" issues.

3. **Rapid Delivery**: Perubahan kode yang berhasil melewati semua quality checks dapat langsung diakses pengguna akhir dalam waktu yang relatif singkat, mendukung prinsip fast feedback loop.

**Quality Gates Implementation:**
Pipeline juga mengintegrasikan multiple quality gates seperti code coverage minimum 80%, PMD static analysis, dan functional testing yang memastikan hanya kode berkualitas tinggi yang sampai ke production environment.

---

# Module 03 - Maintainability

## Refleksi SOLID

### 1. Prinsip SOLID yang Diterapkan

Dalam proyek ini, saya telah menerapkan beberapa prinsip SOLID untuk meningkatkan kualitas dan pemeliharaan kode:

*   **Single Responsibility Principle (SRP)**: Saya memisahkan `CarController` dari `ProductController`. Sebelumnya, kedua kontroler ini berada dalam satu file yang sama atau satu kelas yang sama, yang melanggar SRP karena kelas tersebut memiliki lebih dari satu alasan untuk berubah (mengelola produk dan mobil sekaligus). Dengan memisahkan keduanya ke kelas yang berbeda (`CarController` dan `ProductController`), masing-masing kelas kini hanya bertanggung jawab atas entitasnya sendiri.
*   **Liskov Substitution Principle (LSP)**: Saya menghapus hubungan pewarisan antara `CarController` dan `ProductController`. Sebelumnya, `CarController` melakukan *extends* terhadap `ProductController`, padahal secara semantik "Car" bukanlah substitusi dari "Product" dalam konteks hirarki kontroler ini (misalnya, `CarController` tidak membutuhkan semua metode atau perilaku dari `ProductController`). Menghapus pewarisan ini memastikan bahwa objek kontroler tidak dipaksa memiliki perilaku yang tidak relevan.
*   **Dependency Inversion Principle (DIP)**: Saya mengubah ketergantungan `CarController` dari implementasi konkret (`CarServiceImpl`) menjadi abstraksi/interface (`CarService`). Hal ini terlihat pada penggunaan `@Autowired private CarService carservice;`. Hal ini memungkinkan kontroler untuk tetap stabil meskipun implementasi layanan di bawahnya berubah, dan memudahkan proses *mocking* saat pengujian.
*   **Open-Closed Principle (OCP)**: Dengan menggunakan interface seperti `CarService` dan `ProductService`, sistem menjadi terbuka untuk ekstensi (kita bisa menambahkan implementasi baru seperti `CarDbServiceImpl`) tetapi tertutup untuk modifikasi pada kode klien (seperti `CarController`).
*   **Interface Segregation Principle (ISP)**: Meskipun interface saat ini masih sederhana, pemisahan antara `CarService` dan `ProductService` memastikan bahwa klien hanya bergantung pada metode yang mereka butuhkan. `CarController` tidak perlu tahu tentang metode-metode yang spesifik untuk `Product`.

### 2. Keuntungan Menerapkan SOLID

Penerapan prinsip SOLID memberikan beberapa keuntungan signifikan:

*   **Kemudahan Pemeliharaan (Maintainability)**: Dengan **SRP**, jika terjadi perubahan pada logika `Car`, saya hanya perlu fokus pada `CarController` dan `CarService` tanpa khawatir merusak fungsionalitas `Product`.
*   **Kode Lebih Fleksibel dan Modular**: Berkat **DIP**, saya bisa dengan mudah mengganti implementasi `CarService` (misalnya dari in-memory repository ke database sungguhan) tanpa perlu menyentuh kode di `CarController`. Cukup dengan membuat kelas baru yang mengimplementasikan `CarService`.
*   **Pengujian Lebih Mudah**: Karena kelas-kelas sekarang lebih terfokus (**SRP**) dan bergantung pada interface (**DIP**), pembuatan *unit test* menjadi lebih sederhana karena saya bisa melakukan *mocking* pada dependensi dengan lebih bersih menggunakan tools seperti Mockito.
*   **Struktur Kode Lebih Logis**: Dengan **LSP**, kita menghindari "Hacks" atau perilaku tak terduga yang muncul akibat pewarisan yang dipaksakan, sehingga kode lebih mudah dipahami oleh pengembang lain.

### 3. Kerugian Tidak Menerapkan SOLID

Jika prinsip SOLID tidak diterapkan, beberapa masalah yang mungkin muncul antara lain:

*   **Rigidity (Kekakuan)**: Perubahan kecil di satu bagian kode bisa memaksa perubahan besar di bagian lain. Contohnya, jika `CarController` masih menyatu dengan `ProductController`, perubahan pada routing `Car` atau logika internal `Car` berisiko tinggi mengganggu kestabilan fitur `Product`.
*   **Fragility (Kerapuhan)**: Kode menjadi mudah rusak saat diubah. Tanpa **LSP**, jika kita memiliki hirarki kelas yang tidak tepat (seperti `CarController` yang *extends* `ProductController`), perubahan pada *parent class* dapat merusak perilaku *subclass* secara tidak terduga, yang mengakibatkan bug yang sulit dilacak.
*   **High Coupling (Keterikatan Tinggi)**: Tanpa **DIP**, kontroler akan sangat bergantung pada implementasi spesifik (`CarServiceImpl`). Jika kita ingin mengganti cara penyimpanan data, kita harus mengubah kode di kontroler juga, yang melanggar prinsip modularitas.
*   **Sulit Dipahami (Lower Readability)**: Kode yang melanggar **SRP** cenderung menjadi "God Class" yang sangat besar dan melakukan banyak hal sekaligus. Pengembang baru akan kesulitan memahami alur kerja aplikasi karena logika bisnis yang campur aduk.


# Module 4 - Refactoring and TDD

## Reflesi TDD

## 1. Refleksi terhadap Alur TDD

Pada latihan ini, saya mengikuti alur **Test-Driven Development (TDD)** yang terdiri dari tiga tahap utama yaitu **Red, Green, dan Refactor**. 
Pendekatan TDD menekankan penulisan pengujian terlebih dahulu sebelum mengimplementasikan kode program. 
Menurut saya, alur TDD cukup membantu dalam proses pengembangan perangkat lunak. Dengan menulis test terlebih dahulu, saya menjadi lebih memahami kebutuhan dan perilaku yang diharapkan dari suatu komponen sebelum menuliskan implementasinya. 
Misalnya pada pembuatan model `Order`, saya harus memikirkan terlebih dahulu bagaimana sistem seharusnya menangani kondisi seperti produk yang kosong, status yang tidak valid, atau perubahan status order. 
Hal ini membuat proses implementasi menjadi lebih terarah karena sudah ada gambaran perilaku sistem yang ingin dicapai. 
Selain itu, TDD juga membantu dalam mendeteksi kesalahan lebih awal. Ketika menjalankan test secara berkala, saya dapat langsung mengetahui apakah perubahan kode yang dilakukan sudah sesuai dengan kebutuhan atau justru menyebabkan test gagal. 
Dengan demikian, proses debugging menjadi lebih mudah. Namun, dalam praktiknya saya juga merasakan beberapa tantangan. Terkadang cukup sulit untuk menuliskan test terlebih dahulu ketika struktur implementasi belum sepenuhnya jelas. 
Oleh karena itu, ke depannya saya perlu meluangkan waktu lebih untuk memahami requirement dan merancang skenario pengujian sebelum mulai menulis implementasi kode. Dengan begitu, test yang dibuat dapat lebih merepresentasikan kebutuhan sistem dengan baik. 
Secara keseluruhan, penggunaan TDD membantu meningkatkan kualitas kode karena setiap fitur yang dibuat selalu disertai dengan pengujian yang memastikan fungsionalitasnya berjalan dengan benar.

---

## 2. Refleksi terhadap Prinsip F.I.R.S.T.

Pada tutorial ini saya telah membuat beberapa unit test untuk model, repository, dan service. Secara umum, test yang dibuat sudah mengikuti prinsip **F.I.R.S.T.**, yaitu **Fast, Independent, Repeatable, Self-validating, dan Timely**.

**Fast**  
Test yang dibuat dapat dijalankan dengan cepat karena hanya menguji unit kecil dari program seperti model dan repository, tanpa melibatkan sistem eksternal seperti database atau jaringan.
**Independent**  
Setiap test bersifat independen dan tidak bergantung pada hasil test lainnya. Hal ini dicapai dengan menggunakan metode `@BeforeEach` untuk menginisialisasi data sebelum setiap test dijalankan.
**Repeatable**  
Test dapat dijalankan berulang kali dengan hasil yang konsisten karena tidak bergantung pada faktor eksternal seperti waktu sistem, jaringan, atau kondisi lingkungan tertentu.
**Self-validating**  
Test menggunakan assertion seperti `assertEquals`, `assertSame`, dan `assertThrows` untuk memverifikasi hasil yang diharapkan. Dengan demikian, hasil test dapat langsung menunjukkan apakah test tersebut berhasil atau gagal tanpa perlu pengecekan manual.
**Timely**  
Test ditulis sebelum implementasi kode sebagai bagian dari proses TDD. Hal ini membantu memastikan bahwa implementasi yang dibuat benar-benar memenuhi kebutuhan yang telah didefinisikan dalam test.

Meskipun sebagian besar prinsip F.I.R.S.T. sudah terpenuhi, masih terdapat ruang untuk perbaikan. Ke depannya, saya dapat menambahkan lebih banyak test untuk menangani berbagai edge case atau kondisi batas lainnya agar kualitas pengujian menjadi lebih baik. Selain itu, penamaan test yang lebih deskriptif juga dapat membantu meningkatkan keterbacaan dan pemahaman terhadap tujuan dari setiap test.
Secara keseluruhan, penerapan prinsip F.I.R.S.T. membantu memastikan bahwa test yang dibuat efektif, dapat diandalkan, dan mudah dipelihara.


