import os
import datetime
import cv2
import mediapipe as md

def process_video(video_data):
    file_name = datetime.datetime.now().strftime("%Y-%m-%d_%H-%M-%S")

    path = f"../data/{file_name}.mp4"
    os.makedirs(os.path.dirname(path), exist_ok=True)

    with open(path, "wb") as f:
        f.write(video_data)

    processed_data = count_pushup(path)
    return processed_data

def count_pushup(path):
    md_drawing = md.solutions.drawing_utils
    md_drawing_styles = md.solutions.drawing_styles
    md_pose = md.solutions.pose 

    count = 0
    position = None 

    cap = cv2.VideoCapture(path)

    with md_pose.Pose(
        min_detection_confidence = 0.7,
        min_tracking_confidence = 0.7
    ) as pose:
        while cap.isOpened():
            success, image = cap.read()
            if not success:
                print('Video not found or ended')
                break
            
            image = cv2.cvtColor(cv2.flip(image, 1), cv2.COLOR_BGR2RGB)
            result = pose.process(image)
            
            imlist = []

            if result.pose_landmarks:
                md_drawing.draw_landmarks(
                    image,
                    result.pose_landmarks,
                    md_pose.POSE_CONNECTIONS
                )
                for id, lm in enumerate(result.pose_landmarks.landmark):
                    h, w, _ = image.shape
                    X, Y = int(lm.x * w), int(lm.y * h)
                    imlist.append([id, X, Y])

            if len(imlist) != 0:
                left = abs(imlist[12][2] - imlist[14][2])
                right = abs(imlist[11][2] - imlist[13][2])
                if ((left) >= 75 and (right) >= 75):
                    position = "down"
                if ((left) <= 50 and (right) <= 50) and position == "down":
                    position = "up"
                    count +=1 
                    print(count)

    cap.release()
    return count