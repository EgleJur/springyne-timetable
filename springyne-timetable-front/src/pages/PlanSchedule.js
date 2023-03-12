import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Calendar from "../components/Calendar";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import { FormControl, InputLabel, TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { Select, MenuItem } from "@mui/material";
import dayjs from "dayjs";
import { Alert, Collapse } from "@mui/material";

function PlanSchedulePage() {
  const params = useParams();
  const [schedule, setSchedule] = useState({});
  const [open, setOpen] = useState(false);
  const [startDateValue, setStartDateValue] = useState(null);
  const [endDateValue, setEndDateValue] = useState(null);
  const [startTime, setStartTime] = useState("");
  const [endTime, setEndTime] = useState("");
  const [startDateError, setStartDateError] = useState(false);
  const [endDateError, setEndDateError] = useState(false);
  const [startTimeError, setStartTimeError] = useState(false);
  const [endTimeError, setEndTimeError] = useState(false);
  const [subjectError, setSubjectError] = useState("");
  const [teacherError, setTeacherError] = useState("");
  const [roomError, setRoomError] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");
  const [selectedTeacher, setSelectedTeacher] = useState("");
  const [selectedRoom, setSelectedRoom] = useState("");
  const [teachers, setTeachers] = useState([]);
  const [rooms, setRooms] = useState([]);
  const today = dayjs().format("YYYY-MM-DD");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const times = [1,2,3,4,5,6,7,8,9,10,11,12,13,14];
  const [lessons, setLessons] = useState([]);
  



  useEffect(() => {

  const fetchShedule=()=>{

    fetch("/api/v1/schedules/" + params.id)
  .then((response) => response.json())
  .then((jsonResponse) => setSchedule(jsonResponse));
};
  useEffect(fetchShedule, []);

  const fetchTeachers = () => {
    fetch("/api/v1/teachers/subject?subjectId=" + selectedSubject)
      .then((response) => response.json())
      .then((jsonResponse) => setTeachers(jsonResponse));
  };

  useEffect(fetchTeachers, [selectedSubject]);

  const prefillRooms = () => {
    if (selectedSubject === "") {
      setRooms([]);
    } else {
      let subjectValue = schedule?.group?.program?.subjects?.filter(
        (subject) => subject.subject.id === selectedSubject
      );
      if (!(subjectValue === undefined)) {
        setRooms(subjectValue[0].subject?.rooms);
      }
    }
  };

  useEffect(prefillRooms, [selectedSubject]);

  const fetchLessons = () => {
    fetch("/api/v1/lessons/schedule/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setLessons(jsonResponse));
  };
  useEffect(() => fetchLessons, []);

  const createNewLesson = (e) => {
    e.preventDefault();
    setStartDateError(false);
    setEndDateError(false);
    setSubjectError(false);
    setTeacherError(false);
    setRoomError(false);
    setStartTimeError(false);
    setEndTimeError(false);
    if (
      selectedSubject === null ||
      selectedRoom === null ||
      selectedTeacher === null ||
      startDateValue === null ||
      endDateValue === null ||
      startDateValue > endDateValue ||
      startDateValue < today ||
      startDateValue < schedule?.startDate ||
      endDateValue > schedule?.endDate ||
      startTime === null ||
      endTime === null ||
      endTime < startTime ||
      startTime < schedule?.group?.shift?.starts ||
      endTime > schedule?.group?.shift?.ends
    ) {
      if (selectedSubject === "") {
        setSubjectError(true);
      }
      if (selectedTeacher === "") {
        setTeacherError(true);
      }
      if (selectedRoom === "") {
        setRoomError(true);
      }
      if (startTime === "") {
        setStartTimeError(true);
      }
      if (endTime === "") {
        setEndTimeError(true);
      }
      if (
        startDateValue === null ||
        startDateValue < today ||
        startDateValue < schedule?.startDate ||
        (startDateValue > endDateValue && endDateValue !== null)
      ) {
        setStartDateError(true);
      }
      if (
        endDateValue === null ||
        endDateValue < today ||
        endDateValue < startDateValue ||
        endDateValue > schedule?.endDate
      ) {
        setEndDateError(true);
      }
    } else {
      const startDate = dayjs(startDateValue).format("YYYY-MM-DD");
      const endDate = dayjs(endDateValue).format("YYYY-MM-DD");
      fetch(
        `/api/v1/lessons/schedule/${params.id}?subjectId=${selectedSubject}&teacherId=${selectedTeacher}&roomId=${selectedRoom}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            startDate,
            endDate,
            startTime,
            endTime,
          }),
        }
      ).then((result) => {
        if (result.ok) {
          setStartDateValue(null);
          setEndDateValue(null);
          setStartTime("");
          setEndTime("");
          setSelectedSubject("");
          setSelectedTeacher("");
          setSelectedRoom("");
          setStartDateError(false);
          setEndDateError(false);
          setStartTimeError(false);
          setEndTimeError(false);
          setSubjectError(false);
          setTeacherError(false);
          setRoomError(false);
          setSuccess(true);
          setFailure(false);
          setOpen(false);
          fetchLessons();
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setOpen(false);
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      })
      // .then(setTimeout(() => {
      //   window.location.reload(false);
      // }, 6000))
    }
  };


  return (
    <div className="mx-3">
      <h2 className="my-5">
        Planuoti {schedule?.group?.name} grupės tvarkaraštį
      </h2>

      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai sukurtas
        </Alert>
      </Collapse>

      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko sukurti
        </Alert>
      </Collapse>

      <div className="d-flex">
        <div className="me-auto d-flex">
          <button
            className="btn btn-primary mb-4 me-2"
            onClick={() => setOpen(true)}
          >
            Pridėti naują pamoką
          </button>
        </div>
      </div>

      <Dialog open={open} onClose={() => setOpen(false)}>
        <DialogTitle className="mt-2 mb-2">Pridėti naują pamoką</DialogTitle>
        <DialogContent>
          <form noValidate>
            <DatePicker
              className="mb-3 mt-2"
              label="Pradžios data"
              // inputFormat="mmmm-MM-dd"
              value={startDateValue}
              disablePast
              minDate={schedule?.startDate}
              maxDate={endDateValue === null ? schedule?.endDate : endDateValue}
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
              // inputFormat="yyyy-MM-dd"
              value={endDateValue}
              disablePast
              minDate={
                startDateValue === null ? schedule?.startDate : startDateValue
              }
              maxDate={schedule?.endDate}
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

            <FormControl fullWidth size="small" className="mb-3">
              <InputLabel
                id="select-subject-label"
                error={!!subjectError}
                required
              >
                Pasirinkite dalyką
              </InputLabel>
              <Select
                error={!!subjectError}
                labelId="select-subject-label"
                id="select-subject"
                label="Pasirinkite dalyką"
                fullWidth
                value={selectedSubject}
                onChange={(e) => {
                  setSelectedSubject(e.target.value);
                }}
                required
              >
                {schedule?.group?.program?.subjects?.map((subject) => (
                  <MenuItem
                    value={subject.subject.id}
                    key={subject.subject.id}
                    disabled={subject.subject.deleted}
                  >
                    {subject.subject.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl fullWidth size="small" className="mb-3">
              <InputLabel
                id="select-teacher-label"
                error={!!teacherError}
                required
              >
                Pasirinkite mokytoją
              </InputLabel>
              <Select
                error={!!teacherError}
                labelId="select-teacher-label"
                id="select-teacher"
                label="Pasirinkite mokytoją"
                fullWidth
                value={selectedTeacher}
                onChange={(e) => setSelectedTeacher(e.target.value)}
                required
              >
                {teachers?.map((teacher) => (
                  <MenuItem
                    value={teacher.id}
                    key={teacher.id}
                    disabled={teacher.deleted}
                  >
                    {teacher.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <FormControl fullWidth size="small" className="mb-3">
              <InputLabel id="select-room-label" error={!!roomError} required>
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
                    value={room.id}
                    key={room.id}
                    disabled={room.deleted}
                  >
                    {room.name}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>

            <label htmlFor="select-start-time" className="me-2" required>
              Pradžios laikas *
            </label>
            <Select
              id="select-start-time"
              error={startTimeError}
              value={startTime}
              size="small"
              className="me-2"
              onChange={(e) => setStartTime(parseInt(e.target.value))}
            >
              {times
                .filter(
                  (time) =>
                    time >= schedule?.group?.shift?.starts &&
                    time <= schedule?.group?.shift?.ends
                )
                .map((time) => (
                  <MenuItem key={time} value={time}>
                    {time}
                  </MenuItem>
                ))}
            </Select>

            <label htmlFor="select-end-time" className="me-2" required>
              Pabaigos laikas *
            </label>
            <Select
              id="select-end-time"
              error={!!endTimeError}
              value={endTime}
              size="small"
              className="me-2"
              onChange={(e) => setEndTime(parseInt(e.target.value))}
            >
              {times
                .filter(
                  (time) =>
                    time >= schedule?.group?.shift?.starts &&
                    time <= schedule?.group?.shift?.ends
                )
                .map((time) => (
                  <MenuItem key={time} value={time}>
                    {time}
                  </MenuItem>
                ))}
            </Select>
          </form>
        </DialogContent>
        <DialogActions>
          <button className="btn btn-primary mb-2" onClick={createNewLesson}>
            Pridėti
          </button>
          <button
            className="btn btn-danger me-3 mb-2"
            onClick={() => setOpen(false)}
          >
            Atšaukti
          </button>
        </DialogActions>
      </Dialog>

      <div className="col-md-8 mb-2">
        {schedule?.group?.program?.subjects?.map((sub) => (
          <button
            type="submit"
            className="btn btn-light me-2 mb-2"
            value={sub.id}
            key={sub.id}
            id={sub.id}
            onClick={() => {
              setSelectedSubject(sub.subject.id);
              setOpen(true);
            }}
          >
            {sub.subject.name} {sub.hours}
          </button>
        ))}
      </div>

      <Calendar lessons={lessons}/>
    </div>
  );
}

export default PlanSchedulePage;
