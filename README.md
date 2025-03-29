# 🌤️ Weather App (Java Swing)

แอปพลิเคชันดูสภาพอากาศแบบเรียลไทม์ พร้อมอินเทอร์เฟซกราฟิก (GUI) สวยงามด้วย Java Swing  
รองรับข้อมูล PM2.5, ความชื้น, ลม, พระอาทิตย์ขึ้น/ตก และสามารถบันทึก "เมืองโปรด" ได้

## 🚀 Features

- 🔎 ค้นหาเมืองและดูข้อมูลอากาศแบบเรียลไทม์
- 📊 แสดงข้อมูล: อุณหภูมิ, ความชื้น, PM2.5, ความเร็วลม, ทัศนวิสัย ฯลฯ
- ⭐ เพิ่ม/ลบ เมืองโปรด และเรียกดูย้อนหลังได้
- 🌓 เปลี่ยนธีมกลางวัน/กลางคืนโดยอัตโนมัติ

## 🏗️ Technologies

- Java 17+
- Swing GUI
- Open-Meteo API (ข้อมูลอากาศ)
- AQICN & OpenWeatherMap API (PM2.5)
- GSON & Jackson (JSON Parser)
- Maven (จัดการ dependencies)

## ⚙️ How to Run

### 🖥️ รันผ่าน IDE (เช่น VS Code / IntelliJ)
1. คลิกขวาที่ `App.java` → `Run`
2. หรือรันผ่าน terminal:
```bash
javac -cp ".;lib/*" com/project/App.java
java -cp ".;lib/*" com.project.App
