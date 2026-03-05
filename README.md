# Metato Shop - 한국 의류 쇼핑몰

## 프로젝트 개요

<img width="1480" height="955" alt="쇼핑몰메인" src="https://github.com/user-attachments/assets/a7735158-9953-44bb-b8a7-58ea99c1792c" />


Spring Boot 기반의 한국 의류 온라인 쇼핑몰 웹 애플리케이션입니다.
회원가입, 로그인, 상품 조회, 주문, 리뷰, 1:1 문의 채팅 등 쇼핑몰의 핵심 기능을 구현했습니다.

## 기술 스택
| 분류 | 기술 |
|------|------|
| Backend | Java 19, Spring Boot 3.3.3, Spring Security, Spring Data JPA |
| Frontend | Thymeleaf, HTML/CSS/JavaScript |
| Database | PostgreSQL |
| 인증 | JWT (쿠키 기반), BCrypt |
| Build | Gradle 8.8 |
| 기타 | Lombok, Spring Session, Daum 우편번호 API |

## 주요 기능

### 회원
- 회원가입 (아이디/닉네임/이메일 중복 체크, 비밀번호 암호화)
- JWT 기반 로그인/로그아웃
- 마이페이지

### 상품
- 상품 목록 (페이지네이션)
- 상품 검색
- 카테고리 필터
- 상품 상세 페이지
- 상품 등록 (관리자)
- 상품 이미지 업로드

### 주문
- 상품 주문
- 장바구니
- 주문 내역 관리 (관리자)

### 리뷰
- 상품 리뷰 작성
- 별점 평가 (1~5점)

### 1:1 문의
- 실시간 채팅 (사용자 ↔ 관리자)
- 채팅방 목록 관리 (관리자)

## 기본 계정
| 역할 | 아이디 | 비밀번호 |
|------|--------|----------|
| 관리자 | admin | admin1234 |
| 일반 사용자 | user1 | user1234 |

## 실행 방법
```bash
# 빌드
./gradlew build -x test

# 실행
java -jar build/libs/shop-0.0.1-SNAPSHOT.jar
```

서버 실행 후 http://localhost:5000으로 접속

프로젝트 구조

```bash
src/main/java/com/metato/shop/
├── Item/       # 상품 관리
├── Member/     # 회원 관리 및 인증
├── comment/    # 리뷰
├── sales/      # 주문
├── chat/       # 1:1 문의 채팅
└── Test/       # 테스트 엔티티
```
