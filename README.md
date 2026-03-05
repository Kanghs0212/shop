# Metato Shop - 한국 의류 쇼핑몰

<img width="1480" height="955" alt="쇼핑몰메인" src="https://github.com/user-attachments/assets/b2eb3e9c-147c-4621-8745-a90b2b59b3c4" />


Spring Boot 기반의 한국 의류 쇼핑몰 웹 애플리케이션입니다.

## 기술 스택

- **Backend**: Java 17, Spring Boot 3.3.3, Spring Security, Spring Data JPA
- **Frontend**: Thymeleaf, HTML/CSS/JavaScript
- **Database**: PostgreSQL
- **인증**: JWT (Cookie 기반), BCrypt 비밀번호 암호화
- **빌드**: Gradle 8.8
- **기타**: Lombok, Spring Session JDBC

## 주요 기능

### 사용자 기능
- 회원가입 / 로그인 / 로그아웃 (JWT 인증)
- 상품 목록 조회 (페이지네이션, 카테고리 필터, 검색)
- 상품 상세 페이지 (이미지, 가격, 설명)
- 리뷰 및 별점 등록
- 장바구니 (추가 / 삭제 / 전체 주문)
- 내 주문 목록 확인
- 1:1 문의 채팅

### 관리자 기능
- 상품 등록 / 수정 / 삭제 (이미지 업로드 포함)
- 전체 주문 관리
- 1:1 채팅 응대

## 구동 영상

[![비디오 타이틀](https://img.youtube.com/vi/YFuJSAFlv6o/0.jpg)](https://youtu.be/YFuJSAFlv6o)

## 프로젝트 구조

```
src/main/java/com/metato/shop/
├── ShopApplication.java           # 메인 애플리케이션
├── SecurityConfig.java            # Spring Security 설정 (JWT, CSRF)
├── WebConfig.java                 # 정적 리소스 핸들러
├── DataInitializer.java           # 샘플 데이터 초기화
├── BasicController.java           # 기본 라우트
├── Item/                          # 상품 관리
│   ├── Item.java                  # 상품 엔티티 (카테고리 포함)
│   ├── ItemController.java        # 상품 CRUD 엔드포인트
│   ├── ItemRepository.java
│   ├── ItemService.java
│   └── S3Service.java             # 로컬 파일 업로드 서비스
├── Member/                        # 회원 관리
│   ├── Member.java                # 회원 엔티티
│   ├── MemberController.java      # 인증 엔드포인트
│   ├── MemberRepository.java
│   ├── MemberService.java         # 중복 검사 포함
│   ├── MyUserDetailsService.java
│   ├── CustomUser.java
│   ├── JwtUtil.java               # JWT 토큰 생성/검증
│   └── JwtFilter.java             # JWT 인증 필터
├── comment/                       # 리뷰/댓글
├── sales/                         # 주문 및 장바구니
│   ├── Sales.java                 # 주문 엔티티
│   ├── SalesController.java       # 주문/장바구니 엔드포인트
│   ├── SalesService.java
│   ├── SalesRepository.java
│   ├── CartItem.java              # 장바구니 엔티티 (DB 기반)
│   └── CartItemRepository.java
└── chat/                          # 1:1 채팅

src/main/resources/
├── application.properties         # 앱 설정
├── templates/                     # Thymeleaf 템플릿
│   ├── index.html                 # 메인 페이지
│   ├── list.html                  # 상품 목록
│   ├── detail.html                # 상품 상세
│   ├── login.html                 # 로그인
│   ├── register.html              # 회원가입
│   ├── cart.html                  # 장바구니
│   ├── myOrders.html              # 내 주문 목록
│   ├── orderComplete.html         # 주문 완료
│   ├── sales.html                 # 전체 주문 관리 (관리자)
│   ├── mypage.html                # 마이페이지
│   ├── write.html                 # 상품 등록 (관리자)
│   ├── nav.html                   # 네비게이션 바
│   └── footer.html                # 푸터
└── static/
    ├── main.css                   # 메인 스타일시트
    └── images/                    # 샘플 상품 이미지
```

## 실행 방법

### 사전 요구사항
- Java 17 이상
- PostgreSQL 데이터베이스

### 환경 변수 설정
```
PGHOST=localhost
PGPORT=5432
PGDATABASE=your_database
PGUSER=your_username
PGPASSWORD=your_password
```

### 빌드 및 실행
```bash
# 빌드
./gradlew build -x test

# 실행
java -jar build/libs/shop-0.0.1-SNAPSHOT.jar
```

서버는 `http://localhost:5000` 에서 실행됩니다.

### 기본 계정
| 구분 | 아이디 | 비밀번호 | 권한 |
|------|--------|----------|------|
| 관리자 | admin | admin1234 | ADMIN |
| 사용자 | user1 | user1234 | USER |

첫 실행 시 `DataInitializer`가 자동으로 기본 계정과 샘플 상품 데이터를 생성합니다.

## 디자인

- Apple 스타일의 모던한 UI
- Noto Sans KR 폰트 사용
- 색상: 파란색(#0071e3) 액센트, 흰색 카드, 밝은 회색 배경
- 글래스모피즘 네비게이션 바
- 호버 애니메이션이 적용된 카드 레이아웃

## 데이터베이스

- Hibernate `ddl-auto=update`로 스키마 자동 생성/업데이트
- 주요 엔티티: Member, Item, Comment, Sales, CartItem, ChatRoom, ChatMessage
- 회원 유니크 제약조건: username, displayName, email
