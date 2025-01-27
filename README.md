# NBE3-4-2-Team12
데브코스 3기 4회차 2차프로젝트 12팀

# Git 협업 가이드라인

#### 아침에 첫 작업 시작 전:
```bash
git fetch origin  # 원격 저장소 변경사항 확인
git checkout feature/develop  # develop중인 본인 브랜치로 이동
git pull  # 최신 변경사항 가져오기
git checkout -b feature/new-work  # 새 작업 브랜치 생성
```

## 브랜치명 규칙

### **feature/{feature-name}-{issue-number}**

- ex) 상품 관련 이슈 3번 → feature/item-3

## 개발 시작하기

새로운 기능 개발을 시작하기 전에 다음 단계를 따릅니다:

```bash
# 원격 저장소의 최신 변경사항을 가져옵니다
git fetch origin

# develop 브랜치로 이동합니다
git checkout develop

# 원격의 develop 브랜치를 로컬에 반영합니다
git merge origin/develop

# 새로운 기능 브랜치를 생성합니다
git checkout -b feature/user-authentication
```

## 작업 중 최신 변경사항 동기화하기

다른 팀원의 변경사항을 받아올 때는 다음 두 가지 방법 중 하나를 선택합니다:

### 1. merge 방식

```bash
# 원격의 변경사항을 가져옵니다
git fetch origin develop

# 현재 브랜치에 develop의 변경사항을 머지합니다
git merge origin/develop

# 충돌이 발생한 경우 해결 후 커밋합니다
git add .
git commit -m "merge: develop 브랜치 변경사항 반영"
```

### 2. rebase 방식 (권장)

```bash
# 원격의 변경사항을 가져옵니다
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
```

## 작업 내용 Push하기

```bash
# 변경사항을 스테이징합니다
git add .

# 커밋합니다 (커밋 메시지 컨벤션을 따릅니다)
git commit -m "feat: 사용자 인증 기능 구현"

# 원격 저장소에 푸시합니다
git push origin feature/user-authentication
```

## 커밋 메시지 컨벤션

커밋 메시지는 다음 형식을 따릅니다:

```
type: 제목

본문 (선택사항)

footer (선택사항)
```

- `feat:`: 기능 개발
- `test:`: 테스트 코드
- `fix:`: 버그 수정
- `refactor:*`: 코드 리팩토링
- `docs:`: 문서 작성 및 수정

#### 예시:
```
feat: 회원가입 API 구현

- 이메일 중복 확인 로직 추가
- 비밀번호 암호화 처리
- 회원가입 성공시 환영 이메일 발송

Resolves: #123
See also: #456, #789
```

## Pull Request 생성하기

1. GitHub에서 Pull Request를 생성합니다.
2. PR 제목은 커밋 메시지 컨벤션을 따릅니다.
3. PR 본문에는 다음 내용을 포함합니다:
   - 작업 내용 요약
   - 주요 변경사항
   - 테스트 방법
   - 리뷰어가 중점적으로 봐야 할 부분
4. 팀원들의 코드 리뷰를 기다립니다.
5. 승인을 받은 후 develop 브랜치에 머지합니다.

## 문제 해결하기

### 1. 잘못된 브랜치에서 작업했을 때
```bash
# 현재 변경사항을 임시저장합니다
git stash

# 올바른 브랜치로 이동합니다
git checkout feature/correct-branch

# 임시저장한 변경사항을 적용합니다
git stash pop
```

### 2. 직전 커밋 메시지 수정하기
```bash
git commit --amend -m "feat: 올바른 커밋 메시지"
```

### 3. 이전 커밋으로 되돌리기
```bash
# 특정 커밋으로 되돌리고 새로운 커밋 생성
git revert <commit-hash>

# 또는 현재 브랜치를 특정 커밋으로 강제 이동 (주의: 이력이 삭제됨)
git reset --hard <commit-hash>
```

## IDE에서 Git 사용하기 (IntelliJ IDEA)

IntelliJ IDEA에서는 다음 기능들을 활용할 수 있습니다:

1. Git 툴 윈도우 (Alt + 9)
   - 브랜치 관리
   - 커밋 이력 확인
   - 변경사항 스테이징

2. Commit 툴 윈도우 (Ctrl + K)
   - 변경사항 확인
   - 커밋 메시지 작성
   - 코드 리포맷 및 린트 검사

3. 충돌 해결
   - Merge 충돌 발생 시 시각적 도구 제공
   - 좌측/우측/병합 결과를 쉽게 비교 가능



