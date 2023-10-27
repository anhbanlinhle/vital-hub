from fastapi import APIRouter, File
from models import count_pushup
import os
import datetime

router = APIRouter()

@router.get("/")
async def homepage():
    return {"message": "server ok"}

@router.post("/pushup")
async def process_video_data(video_data: bytes = File()):
    file_name = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    path = f"../data/{file_name}.mp4"
    os.makedirs(os.path.dirname(path), exist_ok=True)

    with open(path, "wb") as f:
        f.write(video_data)

    processed_data = count_pushup(path)
    return {"count": processed_data}