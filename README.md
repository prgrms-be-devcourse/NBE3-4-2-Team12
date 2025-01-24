# NBE3-4-2-Team12
데브코스 3기 4회차 2차프로젝트 12팀

## Git 협업 가이드

#### 개발 시작하기
새로운 기능 개발을 시작하기 전에 다음 단계를 따릅니다:
bashCopy# 원격 저장소의 최신 변경사항을 가져옵니다
git fetch origin

# develop 브랜치로 이동합니다
git checkout develop

# 원격의 develop 브랜치를 로컬에 반영합니다
git merge origin/develop

# 새로운 기능 브랜치를 생성합니다
git checkout -b feature/user-authentication
작업 중 최신 변경사항 동기화하기
다른 팀원의 변경사항을 받아올 때는 다음 두 가지 방법 중 하나를 선택합니다:
1. merge 방식
bashCopy# 원격의 변경사항을 가져옵니다
git fetch origin develop

# 현재 브랜치에 develop의 변경사항을 머지합니다
git merge origin/develop

# 충돌이 발생한 경우 해결 후 커밋합니다
git add .
git commit -m "merge: develop 브랜치 변경사항 반영"
2. rebase 방식 (권장)
bashCopy# 원격의 변경사항을 가져옵니다
git fetch origin develop

# 현재 브랜치의 커밋들을 develop 브랜치 위로 재배치합니다
git rebase origin/develop

# 충돌이 발생한 경우:
# 1. 충돌을 해결합니다
# 2. 해결된 파일을 스테이징합니다
git add .

# 3. rebase를 계속 진행합니다
git rebase --continue

# 4. 만약 rebase를 취소하고 싶다면:
git rebase --abort
작업 내용 Push하기
bashCopy# 변경사항을 스테이징합니다
git add .

# 커밋합니다 (커밋 메시지 컨벤션을 따릅니다)
git commit -m "feat: 사용자 인증 기능 구현"

# 원격 저장소에 푸시합니다
git push origin feature/user-authentication


## 커밋 메시지 컨벤션
<aside>
✅

### 1. 커밋 유형 지정 및 이슈 번호 작성

- 커밋 유형은 영어 **소문자**로 작성하기
    | 커밋 유형 | 의미 |
    | --- | --- |
    | `feat`  | 새로운 기능 추가 |
    | `fix` | 버그 수정 |
    | `refactor` | 코드 리팩토링 |
    | `style`  | 코드 formatting, 세미콜론 누락, 코드 자체의 변경이 없는 경우 |
    | `test` | 테스트 코드, 리팩토링 테스트 코드 추가 |
    | `chore`  | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
    | `rename` | 파일 또는 폴더 명을 수정하거나 옮기는 작업만인 경우 |
    | `remove` | 파일을 삭제하는 작업만 수행한 경우 |
    | `init`  | 초기 설정 작업을 수행한 경우 |
    | `docs`  | 문서를 추가/수정한 경우 |
    
    [예시]  이슈 #3번의 상품 개발
    
    → **feat: 상품 리스트, 검색, … 구현 (#3)**
    

### 2. 제목과 본문을 빈행으로 분리

- 커밋 유형 이후 제목과 본문은 한글로 작성하여 내용이 잘 전달될 수 있도록 할 것
- 본문에는 변경한 내용과 이유 설명 (어떻게 보다는 무엇 & 왜 설명)

### 3. 제목 첫 글자는 대문자로, 끝에는 `.` 금지

### 4. 제목은 짧고 본론만 작성

### 5. 자신의 코드가 직관적으로 바로 파악할 수 있다고 생각하지 말자

</aside>


