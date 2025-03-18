from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
from prophet import Prophet
from datetime import datetime, timedelta
import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

@app.route('/api/forecast', methods=['POST'])
def forecast():
    data = request.json
    historical_data = data.get('historical_data', [])
    days_to_forecast = data.get('days_to_forecast', 30)
    
    # Convert to DataFrame for Prophet
    df = pd.DataFrame(historical_data)
    
    # Rename columns to Prophet expected format
    df = df.rename(columns={'date': 'ds', 'quantity': 'y'})
    
    # Convert string dates to datetime if needed
    if df['ds'].dtype == 'object':
        df['ds'] = pd.to_datetime(df['ds'])
    
    # Initialize and fit model
    model = Prophet(
        yearly_seasonality=True,
        weekly_seasonality=True,
        daily_seasonality=True,
        seasonality_mode='multiplicative',
        changepoint_prior_scale=0.05
    )
    model.fit(df)
    
    # Create future dataframe
    future = model.make_future_dataframe(periods=days_to_forecast)
    
    # Make predictions
    forecast = model.predict(future)
    
    # Convert to response format
    result = {}
    for _, row in forecast.tail(days_to_forecast).iterrows():
        date_str = row['ds'].strftime('%Y-%m-%d')
        result[date_str] = float(row['yhat'])
    
    return jsonify(result)

@app.route('/api/trends', methods=['POST'])
def trends():
    data = request.json
    historical_data = data.get('historical_data', [])
    
    # Convert to DataFrame for Prophet
    df = pd.DataFrame(historical_data)
    
    # Rename columns to Prophet expected format
    df = df.rename(columns={'date': 'ds', 'quantity': 'y'})
    
    # Convert string dates to datetime if needed
    if df['ds'].dtype == 'object':
        df['ds'] = pd.to_datetime(df['ds'])
    
    # Initialize and fit model
    model = Prophet(
        yearly_seasonality=True,
        weekly_seasonality=True,
        daily_seasonality=True,
        seasonality_mode='multiplicative'
    )
    model.fit(df)
    
    # Create future dataframe (using existing dates)
    future = model.make_future_dataframe(periods=0)
    
    # Make predictions
    forecast = model.predict(future)
    
    # Extract trend component
    result = {}
    for _, row in forecast.iterrows():
        date_str = row['ds'].strftime('%Y-%m-%d')
        result[date_str] = float(row['trend'])
    
    return jsonify(result)

@app.route('/api/seasonality', methods=['POST'])
def seasonality():
    data = request.json
    historical_data = data.get('historical_data', [])
    
    # Convert to DataFrame for Prophet
    df = pd.DataFrame(historical_data)
    
    # Rename columns to Prophet expected format
    df = df.rename(columns={'date': 'ds', 'quantity': 'y'})
    
    # Convert string dates to datetime if needed
    if df['ds'].dtype == 'object':
        df['ds'] = pd.to_datetime(df['ds'])
    
    # Initialize and fit model
    model = Prophet(
        yearly_seasonality=True,
        weekly_seasonality=True,
        daily_seasonality=True,
        seasonality_mode='multiplicative'
    )
    model.fit(df)
    
    # Create future dataframe (using existing dates)
    future = model.make_future_dataframe(periods=0)
    
    # Make predictions
    forecast = model.predict(future)
    
    # Extract seasonality components
    result = {}
    for _, row in forecast.iterrows():
        date_str = row['ds'].strftime('%Y-%m-%d')
        # Sum all seasonality components
        seasonality_value = sum([
            row.get('yearly', 0),
            row.get('weekly', 0),
            row.get('daily', 0)
        ])
        result[date_str] = float(seasonality_value)
    
    return jsonify(result)

@app.route('/health', methods=['GET'])
def health():
    return jsonify({"status": "healthy"})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True) 