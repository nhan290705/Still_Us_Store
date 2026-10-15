-- =====================================================================
-- V2: đổi OrderStatus sang 5 bước của khách (chốt 15/10/2026)
--   RECEIVED (Đã ghi nhận) -> PREPARING (Đang chuẩn bị) -> WAITING_SHIPMENT (Chưa giao)
--   -> HANDED_TO_CARRIER (Đã bàn giao ĐVVC) -> DELIVERED (Đã giao); giữ CANCELLED, RETURNED
--
-- Vì sao cần file này: ddl-auto=update tự thêm bảng, cột mới nhưng KHÔNG sửa ràng buộc
-- CHECK cũ của cột enum. Không chạy thì lưu đơn trạng thái mới sẽ lỗi
-- "violates check constraint orders_status_check".
--
-- Ai cần chạy: người có database cũ (đã chạy app trước khi pull bản này).
-- Database mới tạo trống thì KHÔNG cần chạy, Hibernate tự tạo CHECK đúng.
-- Cách chạy: tắt app, mở Query Console trên mmtbistore_db, chạy cả file. Chạy lại lần 2 vẫn an toàn.
-- =====================================================================
BEGIN;

-- 1. Gỡ 3 CHECK cũ
ALTER TABLE orders                 DROP CONSTRAINT IF EXISTS orders_status_check;
ALTER TABLE order_status_histories DROP CONSTRAINT IF EXISTS order_status_histories_from_status_check;
ALTER TABLE order_status_histories DROP CONSTRAINT IF EXISTS order_status_histories_to_status_check;

-- 2. Đổi dữ liệu cũ sang giá trị mới (chưa có đơn thì 0 dòng, vẫn chạy bình thường)
UPDATE orders SET status = CASE status
    WHEN 'PENDING_PAYMENT' THEN 'RECEIVED'
    WHEN 'PAID'            THEN 'RECEIVED'
    WHEN 'PRODUCING'       THEN 'PREPARING'
    WHEN 'PACKED'          THEN 'WAITING_SHIPMENT'
    WHEN 'SHIPPING'        THEN 'HANDED_TO_CARRIER'
    WHEN 'COMPLETED'       THEN 'DELIVERED'
    ELSE status END;

UPDATE order_status_histories SET
  from_status = CASE from_status
    WHEN 'PENDING_PAYMENT' THEN 'RECEIVED'          WHEN 'PAID'      THEN 'RECEIVED'
    WHEN 'PRODUCING'       THEN 'PREPARING'         WHEN 'PACKED'    THEN 'WAITING_SHIPMENT'
    WHEN 'SHIPPING'        THEN 'HANDED_TO_CARRIER' WHEN 'COMPLETED' THEN 'DELIVERED'
    ELSE from_status END,
  to_status = CASE to_status
    WHEN 'PENDING_PAYMENT' THEN 'RECEIVED'          WHEN 'PAID'      THEN 'RECEIVED'
    WHEN 'PRODUCING'       THEN 'PREPARING'         WHEN 'PACKED'    THEN 'WAITING_SHIPMENT'
    WHEN 'SHIPPING'        THEN 'HANDED_TO_CARRIER' WHEN 'COMPLETED' THEN 'DELIVERED'
    ELSE to_status END;

-- 3. Gắn CHECK mới, khớp enum OrderStatus mới
ALTER TABLE orders ADD CONSTRAINT orders_status_check CHECK (status IN
  ('RECEIVED','PREPARING','WAITING_SHIPMENT','HANDED_TO_CARRIER','DELIVERED','CANCELLED','RETURNED'));
ALTER TABLE order_status_histories ADD CONSTRAINT order_status_histories_from_status_check CHECK (from_status IN
  ('RECEIVED','PREPARING','WAITING_SHIPMENT','HANDED_TO_CARRIER','DELIVERED','CANCELLED','RETURNED'));
ALTER TABLE order_status_histories ADD CONSTRAINT order_status_histories_to_status_check CHECK (to_status IN
  ('RECEIVED','PREPARING','WAITING_SHIPMENT','HANDED_TO_CARRIER','DELIVERED','CANCELLED','RETURNED'));

COMMIT;
