**Đặt tên branch** <br>
```feat/tenNguoiLam_ddmm_task```

**Tạo branch mới từ branch đang ở** <br>
```git checkout -b feat/tenNguoiLam_ddmm_task```

**Pull code ở branch main mỗi ngày** <br>
```git checkout main``` <br>
```git pull``` <br>
*Nếu sang nhánh có sẵn và đang làm* <br>
```git checkout nhánh cần làm``` <br>
```git merge main --no-ff``` <br>
*Nếu sang 1 nhánh mới để làm task mới* <br>
```git checkout -b feat/tenNguoiLam_ddmm_task``` <br>

**Đẩy code** <br>
*Check những file đã thay đổi* <br>
```git status``` <br>
*Chọn file để commit* <br>
```git add + tên file``` <br>
*Chọn tất cả những thứ đã thay đổi* <br>
```git add .``` <br>
*Commit* <br>
```git commit -m "mô tả (viết đầy đủ những thứ đã làm)"``` <br>
*Trước khi push nhớ check lại với nhánh main để xem còn conflict với main không* <br>
```git checkout main``` <br>
```git pull``` <br>
```git checkout nhánh chuẩn bị push``` <br>
```git merge main --no-ff``` <br>
*Push lên remote* <br>
```git push origin tên_nhánh``` <br>
*Sau khi push, lên web sẽ có nút compare & merge request => bấm => ghi nội dung* <br>
<kbd>Compare & Merge request</kbd>

<h1>ĐÉO ĐƯỢC PUSH LÊN MASTER</h1>
<h1>ĐÉO CÓ PUSH --FORCE MASTER</h1>
