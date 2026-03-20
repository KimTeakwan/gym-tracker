# 🏋️‍♂️ gym-tracker (오운완 기록 및 관리 서비스)

6월 바디프로필 성공을 목표로 하는 개인별 맞춤형 운동 관리 애플리케이션입니다. 

## 📂 프로젝트 유형
* **유형:** 풀스택(Full-Stack) 운동 관리 웹 애플리케이션
* **핵심 기능:**
    * 🔐 **이메일 인증 시스템:** 회원가입 시 입력한 이메일로 인증 링크를 발송하여 계정을 활성화하는 보안 로직을 적용하였습니다.
    * 👤 **개인별 데이터 격리:** 로그인한 사용자 본인의 운동 기록(웨이트/유산소)만 조회하고 관리할 수 있도록 개인화 필터링을 구현하였습니다.
    * 🏋️‍♂️ **부위별 웨이트 로그:** 데이터베이스 예약어 충돌을 방지한 설계와 총 훈련 볼륨 자동 계산 기능을 탑재하였습니다.
    * 🏃‍♂️ **유산소 실시간 트래커:** MET 5.0 지표를 기반으로 한 정밀한 소모 칼로리 자동 계산 로직을 적용하였습니다.
    * 🗑️ **안전한 삭제 로직:** 소유권 검증 및 예외 처리를 통해 본인의 기록만 안전하게 삭제할 수 있도록 구현하였습니다.
    * 📊 **데이터 시각화:** Chart.js를 활용하여 주간 훈련 볼륨 및 요일별 운동량 변화를 직관적으로 확인할 수 있습니다.

## 🛠️ 기술 스택

### **Backend**
* **Language:** Java
* **Framework:** Spring Boot (Spring Data JPA)
* **Security:** Spring Security (BCrypt 암호화 및 계정 활성화 상태 관리)
* **Mail:** Spring Boot Starter Mail (SMTP 기반 인증 메일 발송 시스템)

### **Frontend**
* **Template Engine:** Thymeleaf
* **Styling:** Bootstrap 5.3.0 및 Glassmorphism UI (반투명 유리 효과 및 배경 영상 적용)
* **Library:** Chart.js, Vanilla JavaScript (비밀번호 가시성 토글 및 유효성 검사 로직 포함)

### **Database & Storage**
* **RDBMS:** TiDB Cloud (AWS Tokyo Region) ☁️
    * 클라우드 데이터베이스 마이그레이션을 통해 장소에 구애받지 않는 데이터 접근성을 확보하였습니다.
* **Storage:** localStorage (브라우저 로컬 저장소를 활용한 타이머 세션 유지)

### **Environment & Tools**
* **OS:** macOS (MacBook Air M4) 💻
* **Security:** VS Code `launch.json`을 활용하여 DB 비밀번호 및 메일 앱 비밀번호 등 민감한 정보를 환경 변수로 안전하게 관리합니다.
* **IDE/Tools:** VS Code, Terminal, TiDB SQL Editor

---

## 💡 개발 로그 (Update Highlights)

> "단순한 기록 서비스를 넘어, 보안과 개인화가 강화된 완성도 높은 서비스를 구축하는 데 집중하였습니다."
> 
> 1. **보안 강화:** Spring Security를 도입하여 이메일 인증이 완료되지 않은 계정의 접근을 제한하는 등 보안 품질을 높였습니다.
> 2. **사용자 경험 개선:** 회원가입 시 실제 이메일을 통한 본인 인증 과정을 추가하여 신뢰할 수 있는 사용자 환경을 조성하였습니다.
> 3. **데이터 무결성:** 데이터베이스의 연관 관계 설정을 통해 모든 운동 데이터에 소유권을 부여하고, 철저하게 개인화된 대시보드를 제공합니다.
> 4. **인터페이스 최적화:** 배경 영상과 반투명 카드 레이아웃을 조화시켜 시각적으로 몰입감 있는 UI를 완성하였습니다.

## 🖥️ 화면 미리보기 (ScreenShot)

<img src="./src/main/resources/static/images/1.png" width="800">