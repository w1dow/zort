import cv2
import os
import uuid
# extracts frame from a video for testing the model

def extract_frames(video_path, target_frame_count=15):
    output_folder = os.path.join(os.getcwd(), "output")
    os.makedirs(output_folder, exist_ok=True)
    cap = cv2.VideoCapture(video_path)

    total_frames = int(cap.get(cv2.CAP_PROP_FRAME_COUNT))
    if total_frames == 0:
        cap.release()
        return

    interval = max(total_frames // target_frame_count, 1)
    count = 0
    saved = 0

    while True:
        ret, frame = cap.read()
        if not ret:
            break
        if count % interval == 0 and saved < target_frame_count:
            random_filename = f"{uuid.uuid4().hex}.jpg"
            cv2.imwrite(os.path.join(output_folder, random_filename), frame)
            saved += 1
        count += 1

    cap.release()

if __name__ == "__main__":
    extract_frames("inp.mp4", 15)
