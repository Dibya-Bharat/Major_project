import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder

# Load dataset
data = pd.read_excel("sample_data.xlsx")

# Preprocessing
# Encode categorical variables
label_encoders = {}
for col in ["state", "category"]:
    le = LabelEncoder()
    data[col] = le.fit_transform(data[col])
    label_encoders[col] = le

# Split data
X = data.drop(["place1", "place2", "place3"], axis=1)
y = data[["place1", "place2", "place3"]]

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

# Train model (example using RandomForestClassifier)
model = RandomForestClassifier(n_estimators=100, random_state=42)
model.fit(X_train, y_train)

# User input (state, category, rating)
user_input_state = "Uttarakhand"  # Replace with actual user input
user_input_category = "Religious"
user_input_rating = 4.5

# Process user input
user_input_encoded = pd.DataFrame({
    "state": [label_encoders["state"].transform([user_input_state])[0]],
    "category": [label_encoders["category"].transform([user_input_category])[0]],
    "rating": [user_input_rating]
})

# Get recommendations
predictions = model.predict(user_input_encoded)

print("Top 3 recommended places:", predictions)
