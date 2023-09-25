<h1>Code chức năng được giao</h1>

|Action|Command
|-|-
|*Đặt tên branch*|```feat/owner_ddmm_task```
|*Tạo branch mới từ branch đang ở*|```git checkout -b your_branch```

<h1>Lưu ý checkout</h1>

|Action|Command
|-|-
|*Trước khi checkout sang nhánh khác, <br> phải kiểm tra nhánh hiện tại <br> có còn file nào chưa commit không <br> Nếu không các file chưa commit <br> sẽ bị lôi sang nhánh mới => nguy hiểm <br> Commit như ở dưới đã mô tả*|```git status```

<h1>Pull code ở branch main hàng ngày</h1>

|Action|Command
|-|-
|*Trước khi checkout sang main, <br> phải kiểm tra nhánh hiện tại <br> có còn file nào chưa commit không*|```git status```
|*Prepare*|```git checkout main```<br>```git pull```
|*Nếu sang nhánh có sẵn và đang làm*|```git checkout your_branch```<br>```git merge main --no-ff``` <br> Nếu có conflict phải resolve conflict rồi commit
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


<h1>KHÔNG PUSH LÊN MASTER</h1>
<h1>KHÔNG PUSH --FORCE MASTER</h1>
