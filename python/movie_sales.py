import psycopg2
from datetime import datetime

DB_CONFIG = {
	"host":"localhost",
	"port":5432,
	"dbname":"mydb",
	"user":"admin",
	"password":"secret"
}

def query_sales_for_date(target_date):
	query = """
		SELECT 
			s.show_date, 
			t.name as theatre_name , 
			SUM(tickets * ticket_price) as total_sales
		FROM showings s 
		JOIN theatres t ON s.theatre_id = t.theatre_id
		WHERE s.show_date = %s
		GROUP by s.show_date, t.name
        ORDER BY total_sales DESC
        LIMIT 1
	
	"""
	try:
		with psycopg2.connect(**DB_CONFIG) as conn:
			with conn.cursor() as cur:
				cur.execute(query, (target_date,))
				row = cur.fetchone()
				show_date, theatre_name, total_sales = row
				print(f"Date: {show_date} \nTheatre: {theatre_name} \nSales: ${total_sales}")


	except Exception as e:
		print("Error: ", e)


#Must be validted and updated to ISO 8601 format
def validate_input_and_update_date(validate_date):
    try:
        dt = datetime.strptime(validate_date, "%m/%d/%Y")
        return dt.strftime("%Y-%m-%d")
    except ValueError:
        raise ValueError("Date must be in MM/DD/YYYY format")


if __name__ == "__main__":
	date_input = input("Enter date (MM/DD/YYYY): ").strip()
	formatted_date = validate_input_and_update_date(date_input)
	query_sales_for_date(formatted_date)