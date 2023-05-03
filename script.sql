-- use budgets;
-- select * from budgets;
-- INSERT INTO budgets (user_id, name, `limit`) VALUES (2, 'Shopping', 100.00);
-- use transactions;
-- select * from transactions;
-- use login;
-- select id From login.users;
-- ALTER TABLE transactions MODIFY COLUMN date long;

-- INSERT INTO transactions (user_id, description, amount, is_expense, date)
-- VALUES (2, 'Gaming', 500.00, true, '2023-05-03');
-- select * from transactions;

-- INSERT INTO transactions (user_id, description, amount, is_expense, date) VALUES (1, 'Entertainment', 50.00, true, '2023-04-18');
-- INSERT INTO transactions (user_id, description, amount, is_expense, date) VALUES (2, 'Travel', 450.00, true, '2023-04-17');
-- INSERT INTO transactions (user_id, description, amount, is_expense, date) VALUES (3, 'Home Improvement', 75.00, true, '2023-04-16');
use login;
select * from users;
use budgets;
DESCRIBE budgets;
ALTER TABLE budgets MODIFY COLUMN `Limit` INTEGER NOT NULL DEFAULT 0;

use transactions;
describe transactions;
