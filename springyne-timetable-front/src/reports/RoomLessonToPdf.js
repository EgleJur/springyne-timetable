import React, { useState, useEffect } from "react";
import Dialog from "@mui/material/Dialog";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import DialogActions from "@mui/material/DialogActions";
import { FormControl, InputLabel, TextField, Select, MenuItem } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import dayjs from "dayjs";
import { apiUrl } from "../App";

function RoomLessonToPdf(props) {
  const { open, handleClose, title } = props;
  const [startDateValue, setStartDateValue] = useState(null);
  const [endDateValue, setEndDateValue] = useState(null);
  const [startDateError, setStartDateError] = useState(false);
  const [endDateError, setEndDateError] = useState(false);
  const [roomError, setRoomError] = useState("");
  const [selectedRoom, setSelectedRoom] = useState("");
  const [rooms, setRooms] = useState([]);

  const fetchRooms = () => {
    fetch(`${apiUrl}/api/v1/rooms/`)
      .then((response) => response.json())
      .then((jsonResponse) => setRooms(jsonResponse));
  };

  useEffect(fetchRooms, []);

  const roomsToPdf = (e) => {
    e.preventDefault();
    setStartDateError(false);
    setEndDateError(false);
    setRoomError(false);
    const starts = dayjs(startDateValue).format("YYYY-MM-DD");
    const ends = dayjs(endDateValue).format("YYYY-MM-DD");
    if (
      selectedRoom === null ||
      selectedRoom === "" ||
      startDateValue === null ||
      endDateValue === null ||
      startDateValue > endDateValue
    ) {
      if (selectedRoom === "" || selectedRoom === null) {
        setRoomError(true);
      }
      if (
        startDateValue === null ||
        (startDateValue > endDateValue && endDateValue !== null)
      ) {
        setStartDateError(true);
      }
      if (endDateValue === null || endDateValue < startDateValue) {
        setEndDateError(true);
      }
    } else {
      fetch(
        `${apiUrl}/api/v1/lessons/rooms/export/pdf?roomName=${selectedRoom}&startDate=${starts}&endDate=${ends}`
      )
        .then((response) => {
          if (response.ok) {
            setStartDateValue(null);
            setEndDateValue(null);
            setSelectedRoom("");
            setStartDateError(false);
            setEndDateError(false);
            setRoomError(false);
            handleClose();
            return response.blob();
          }
          throw new Error("Network response was not ok.");
        })
        .then((blob) => {
          const url = window.URL.createObjectURL(blob);
          window.open(url);
          let link = document.createElement('a');
          link.href = url;
          link.download = `RoomPDF`;
          link.click();
        })
        .catch((error) => {
          console.error("There was a problem with the fetch operation:", error);
        });
    }
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>{title}</DialogTitle>
      <DialogContent>
        <form noValidate>
          <DatePicker
            className="mb-3 mt-2"
            label="Pradžios data"
            inputFormat="yyyy-MM-dd"
            value={startDateValue}
            onChange={(newValue) => {
              setStartDateValue(newValue);
            }}
            renderInput={(params) => (
              <TextField
                fullWidth
                size="small"
                required
                {...params}
                error={!!startDateError}
              />
            )}
          />
          <DatePicker
            className="mb-3"
            label="Pabaigos data"
            inputFormat="yyyy-MM-dd"
            value={endDateValue}
            minDate={
              startDateValue !== null ? startDateValue : ""
            }
            onChange={(newValue) => {
              setEndDateValue(newValue);
            }}
            onError={() => setEndDateError(true)}
            renderInput={(params) => (
              <TextField
                fullWidth
                size="small"
                required
                {...params}
                error={!!endDateError}
              />
            )}
          />
          <FormControl fullWidth size="small" className="mb-3" required>
            <InputLabel
              id="select-teacher-label"
              error={!!roomError}
              required
            >
              Pasirinkite kabinetą
            </InputLabel>
            <Select
              error={!!roomError}
              labelId="select-room-label"
              id="select-room"
              label="Pasirinkite kabinetą"
              fullWidth
              value={selectedRoom}
              onChange={(e) => setSelectedRoom(e.target.value)}
              required
            >
              {rooms?.map((room) => (
                <MenuItem
                  value={room.name}
                  key={room.name}
                  disabled={room.deleted}
                >
                  {room.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

        </form>
      </DialogContent>
      <DialogActions>
        <button
          className="btn btn-primary me-3 mb-2"
          onClick={roomsToPdf}
        >
          Spausdinti
        </button>
        <button
          className="btn btn-danger me-3 mb-2"
          onClick={() => {
            handleClose();
            setStartDateValue(null);
            setEndDateValue(null);
            setSelectedRoom("");
            setStartDateError(false);
            setEndDateError(false);
            setRoomError(false);
          }}
        >
          Atšaukti
        </button>
      </DialogActions>

    </Dialog>
  );
}
export default RoomLessonToPdf;