# Community
사용자들간에 커뮤니티를 구성하여 게시글로 소통할 수 있도록 만든 게시판 서비스입니다.

## 개발 기간
25.01.13 ~ 25.02.17 (5주간)

## 개발 환경  
- JAVA
- Spring
- MYSQL
- GIT

## 주요 기능
- [x] 회원 가입<br>
   회원 가입한 사용자는 로그인 할 수 있으며 커뮤니티를 이용할 수 있는 권한을 얻는다.
   이메일 인증을 거쳐서 회원 가입을 진행한다.

 - [x] 가입 시 dto에 유효성 검사 추가하기
   아이디 길이 (4자 이상)
   비밀번호 암호화하여 저장
   이름, 생년월일 필수

- [x] 로그인<br>
   로그인하여 커뮤니티의 기능들을 이용
   JWT 토큰과 Spring Security 이용하여 커뮤니티 이용시 사용자 인증
   
- 게시글 관리<br>
   - [x] 게시글 작성<br>
      제목 20자 이내
      특수문자 금지
      USER 권한 있는 사용자만 작성 가능
      isDisplay 컬럼으로 해당 게시글에 대한 공개 여부 설정 가능
      카테고리 별로 작성
   - [x] 게시글 목록 조회<br>
      비가입자는 게시글 조회만 가능
      pagenation : 페이지 당 10건 씩 조회
      공개 여부에 따라서 디스플레이 N 인 것은 노출 안 함
   - [x] 게시글 편집<br>
     작성자만 편집 가능
   - [x] 게시글 삭제<br>
      작성자만 삭제 가능
      deleted date에 삭제한 날짜 입력
      여러 개 삭제 가능 : 자신이 작성한 글, 존재하는 게시글에 한해서만

- [x] 게시물 검색 기능<br>
   제목, 작성자, 기간 검색을 통하여 게시글 검색 리스트 보여준다.
   pagenation : 페이지 당 10건 씩 조회
  
## 추가
1. 이메일 인증시 Redis 사용해보기
2. 댓글
3. Admin 계정 추가
4. Test Code 작성
   


## Trouble Shooting




## ERD

![community_erd](https://github.com/user-attachments/assets/52529ad0-a0c2-48fb-b1bc-aa0ccfd1e84a)



   
