import pandas as pd
from pathlib import Path

data = [
    ["F1.2a", "rating = 4.5", "Normal valid rating"],
    ["F1.2b", "rating = 0.0", "Below threshold (invalid)"],
    ["F1.2c", "rating = 0.5", "Boundary: minimum acceptable"],
    ["F1.2d", "rating = 5.5", "Exceeds max (invalid)"],
    ["F1.2e", 'restaurantId = "123"', "Valid ID"],
    ["F1.2f", 'restaurantId = ""', "Empty string (invalid)"],
    ["F1.2g", "restaurantId = null", "Null value (invalid)"],
    ["F1.2h", 'restaurantId = "   "', "Whitespace only (invalid)"],
    ["F1.2i", "firebaseUser = null", "User not logged in (invalid)"],
]

df = pd.DataFrame(data, columns=["Test ID", "Input Value", "Description"])

csv_path = Path("app/test-reports/givereviewactivity_inputs.csv")
csv_path.parent.mkdir(parents=True, exist_ok=True)
df.to_csv(csv_path, index=False)

print(f"✅ CSV saved to {csv_path}")