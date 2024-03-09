import cv2
import mediapipe as mp
import pyautogui

interrupt_flag = False

def virtual_mouse(MainWindow):
    global interrupt_flag
    cap = cv2.VideoCapture(0)
    hand_detector = mp.solutions.hands.Hands()
    drawing_utils = mp.solutions.drawing_utils
    app_window_geometry = MainWindow.geometry()
    screen_width, screen_height = pyautogui.size()
    index_y = 0

    try:
        while not interrupt_flag:
            _, frame = cap.read()
            frame = cv2.flip(frame, 1)
            frame_height, frame_width, _ = frame.shape

            # Convert the BGR frame to RGB
            rgb_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

            # Process the frame using the hand detector
            output = hand_detector.process(rgb_frame)
            hands = output.multi_hand_landmarks

            if hands:
                for hand in hands:
                    drawing_utils.draw_landmarks(frame, hand)
                    landmarks = hand.landmark

                    for id, landmark in enumerate(landmarks):
                        x = int(landmark.x * frame_width)
                        y = int(landmark.y * frame_height)

                        if id == 8:
                            # Calculate the index_x and index_y for PyAutoGUI mouse movement
                            index_x = int(screen_width / frame_width * x)
                            index_y = int(screen_height / frame_height * y)
                            pyautogui.moveTo(index_x, index_y)
                            if app_window_geometry.left() <= index_x <= app_window_geometry.right() and \
                                    app_window_geometry.top() <= index_y <= app_window_geometry.bottom():
                                pyautogui.moveTo(index_x, index_y)

                        if id == 4:
                            # Calculate thumb_x and thumb_y for PyAutoGUI click
                            thumb_x = int(screen_width / frame_width * x)
                            thumb_y = int(screen_height / frame_height * y)
                            if app_window_geometry.left() <= index_x <= app_window_geometry.right() and \
                                    app_window_geometry.top() <= index_y <= app_window_geometry.bottom():
                                pyautogui.moveTo(index_x, index_y)

                            # Check the distance between index and thumb landmarks
                            if abs(index_y - thumb_y) < 80:
                                pyautogui.click()

                        if id == 20:
                            # Calculate right_x and right_y for PyAutoGUI right-click
                            right_x = screen_width / frame_width * x
                            right_y = screen_height / frame_height * y
                            if app_window_geometry.left() <= index_x <= app_window_geometry.right() and \
                                    app_window_geometry.top() <= index_y <= app_window_geometry.bottom():
                                pyautogui.moveTo(index_x, index_y)

                            # Check the distance between thumb and right landmarks
                            if abs(index_y - right_y) < 50:
                                pyautogui.rightClick()

        # Release the camera when the loop is exited
        cap.release()
    except KeyboardInterrupt:
        cap.release()
