<h1>Code chức năng được giao</h1>

**Đặt tên branch** <br>
```feat/owner_ddmm_task```

**Tạo branch mới từ branch đang ở** <br>
```git checkout -b feat/owner_ddmm_task```

<h1>Pull code ở branch main hàng ngày</h1>

|Action|Command
|-|-
|*Prepare*|```git checkout main```<br>```git pull```
|*Nếu sang nhánh có sẵn và đang làm*|```git checkout your_branch```<br>```git merge main --no-ff```
|*Nếu sang 1 nhánh mới để làm task mới*|```git checkout -b feat/owner_ddmm_task```

<h1>Đẩy code</h1>

|Action|Command
|-|-
|*Check những file đã thay đổi*|```git status```
|*Chọn file để commit*|```git add file_name```
|*Chọn tất cả những thứ đã thay đổi*|```git add .```
|*Commit*|```git commit -m "mô tả (viết đầy đủ những thứ đã làm)"```
|*Trước khi push nhớ check lại với `main` để xem còn conflict không*|```git checkout main``` <br>```git pull``` <br>```git checkout your_branch``` <br>```git merge main --no-ff``` <br>
|*Push lên remote*|```git push origin your_branch```

*Sau khi push, lên web sẽ có nút <kbd>Compare & Merge request</kbd> => bấm => ghi nội dung* <br>


<h1>ĐÉO ĐƯỢC PUSH LÊN MASTER</h1>
<h1>ĐÉO CÓ PUSH --FORCE MASTER</h1>
