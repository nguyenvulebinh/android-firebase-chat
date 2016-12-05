# RivChat - Android chat app base on Firebase.  
### Tính năng  
**1. Quản lý tài khoản**  
* Đăng ký: Cho phép user đăng ký tài khoản bằng email và password
* Đăng nhập: user phải đăng nhập mới được sử dụng app
* Quên mật khẩu: Chọn reset mật khẩu thì server sẽ gửi 1 mật khẩu mới (random 8 số) về email đã đăng ký.
* Sửa thông tin tài khoản: Cho phép đổi username, ảnh đại diện

**2. Chat, chat nhóm**  
Giao diện chính khi vào app là 1 activity gồm 2 tab: Bạn bè, nhóm  
<img src='https://github.com/HieuApp/android-firebase-chat/blob/master/rivchat.png' width='300'/>  
Hình 1. Giao diện chính của app chat
* Bạn bè: danh sách dạng list có avatar, tên, text chat cuối cùng (nếu đã từng chat). Cho phép thêm bạn, xóa bạn
* Nhóm: danh sách dạng list, tên nhóm, avatar là chữ cái đầu của tên nhóm.
	- Khi bấm vào 1 item(bạn bè hoặc nhóm) thì mở ra màn hình chat.
	- Thành viên có thể rời khỏi nhóm
	- Admin(người tạo nhóm): kích thành viên khỏi nhóm, xóa nhóm, thêm thành viên  
<img src='https://github.com/HieuApp/android-firebase-chat/blob/master/ql-nhom.jpg' width='300'/>  
Hình 2. popup quản lý thành viên nhóm.  

**3. Notification**  
* Hiển thị notification trên statusbar khi có tin nhắn mới
* Tin nhắn chưa đọc bôi đậm  

### Yêu cầu  
* PTTK HT: Usecase, Biểu đồ trình tự, hoạt động, biểu đồ lớp. 

