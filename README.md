# 🏋️‍♂️ gym-tracker (오운완 기록 및 관리 서비스)

6월 바디프로필 성공을 목표로 하는 개인별 맞춤형 운동 관리 애플리케이션입니다. 🔥

## 📂 프로젝트 유형
* **유형:** 풀스택(Full-Stack) 운동 관리 웹 애플리케이션
* **핵심 기능:**
    * 🏋️‍♂️ 부위별 웨이트 운동 로그 기록 및 관리
    * 🏃‍♂️ 유산소 운동 실시간 트래커 (운동 시간 및 소모 칼로리 자동 계산 로직 적용)
    * 🗑️ 웨이트 및 유산소 개별 운동 기록 삭제 기능 (라우팅 및 예외 처리 완비)
    * ⏱️ 전체 운동 세션 스톱워치 및 세트 사이 휴식 타이머
    * 📊 Chart.js를 활용한 주간 운동량 변화 시각화 (총 훈련 볼륨 등)
    * ⚡ 자주 하는 종목 퀵 버튼 및 숫자 증감 조절 기능

## 🛠️ 기술 스택

### **Backend**
* **Language:** Java
* **Framework:** Spring Boot (Spring Data JPA)
* **ORM:** Hibernate (JPA)

### **Frontend**
* **Template Engine:** Thymeleaf
* **Styling:** Bootstrap 5.3.0
* **Library:** Chart.js (데이터 시각화), Vanilla JavaScript

### **Database & Storage**
* **RDBMS:** TiDB Cloud (AWS Tokyo Region) ☁️ 
  * 로컬 DB에서 클라우드 DB로 성공적으로 마이그레이션하여 장소에 구애받지 않는 데이터 접근성 확보
* **Storage:** localStorage (브라우저 로컬 저장소를 활용한 탭 전환 시 타이머 세션 유지)

### **Environment & Tools**
* **OS:** macOS (MacBook Air M4) 💻
* **Security:** VS Code `launch.json`을 활용한 환경 변수(DB 비밀번호) 보안 관리 적용
* **IDE/Tools:** VS Code, Terminal