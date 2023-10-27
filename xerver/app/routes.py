from fastapi import APIRouter, File
from models import count_pushup, speedup_video
import os
import datetime

router = APIRouter()

@router.get("/")
async def homepage():
    return {"message": "server ok"}

@router.post("/pushup")
async def process_video_data(video_data: bytes = File()):
    file_name = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")
    old = f"../data/original/{file_name}.mp4"
    os.makedirs(os.path.dirname(old), exist_ok=True)
    new = f"../data/speedup/{file_name}.mp4"
    os.makedirs(os.path.dirname(new), exist_ok=True)

    with open(old, "wb") as f:
        f.write(video_data)

    speedup_video(old, new)
    processed_data = count_pushup(new)
    return {"count": processed_data}