from fastapi import APIRouter, File
from models import process_video

router = APIRouter()

@router.get("/")
async def homepage():
    return {"message": "server ok"}

@router.post("/pushup")
async def process_video_data(video_data: bytes = File()):
    processed_data = process_video(video_data)
    return {"count": processed_data}