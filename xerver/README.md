# Pushup-counter

This project is a Python server that receives a video from a client, processes the video, and returns some data.

## Installation

1. Clone the repository
2. Install the dependencies with `pip install -r requirements.txt`

## Usage
1. Go to the app directory with `cd app`
2. Run the server with `uvicorn main:app --reload` or `python3 main.py`

Send a **POST** request to `http://localhost:7979/pushup` with the video data in the request body. 

The server will process the video and return the processed data in the response.
