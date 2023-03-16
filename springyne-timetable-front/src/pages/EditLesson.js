import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Calendar from "../components/Calendar";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import { FormControl, InputLabel, TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers";
import { Select, MenuItem } from "@mui/material";
import dayjs from "dayjs";
import { Alert, Collapse } from "@mui/material";
import FormControlLabel from "@mui/material/FormControlLabel";
import FormLabel from "@mui/material/FormLabel";
import Checkbox from "@mui/material/Checkbox";
import { apiUrl } from "../App";

function EditLessonPage() {
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
  const times = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14];
  const [lessons, setLessons] = useState([]);

  const [openEdit, setOpenEdit] = useState(false);
  const [repeats, setRepeats] = useState(false);

  const fetchShedule = () => {
    fetch(`${apiUrl}/api/v1/schedules/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSchedule(jsonResponse));
  };
  useEffect(fetchShedule, []);

  const fetchTeachers = () => {
    fetch(`${apiUrl}/api/v1/teachers/subject?subjectId=` + selectedSubject)
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
    fetch(`${apiUrl}/api/v1/lessons/schedule/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setLessons(jsonResponse));
  };
  useEffect(() => fetchLessons, []);

  const editLesson = (e) => {
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
        `${apiUrl}/api/v1/lessons/schedule/${params.id}?subjectId=${selectedSubject}&teacherId=${selectedTeacher}&roomId=${selectedRoom}`,
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
            repeats,
          }),
        }
      ).then((result) => {
        if (result.ok) {
          setRepeats(false);
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
          setOpenEdit(false);
          fetchLessons();
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setOpenEdit(false);
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
        }
      });
      // .then(setTimeout(() => {
      //   window.location.reload(false);
      // }, 6000))
    }
  };

  const handleChange = (event) => {
    setRepeats(event.target.checked);
  };

  return (
    <div className="mx-3">
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

      {/* <div className="d-flex">
            <div className="me-auto d-flex">
              <button
                className="btn btn-primary mb-4 me-2"
                onClick={() => setOpenEdit(true)}
              >
                Redaguoti pamoką
              </button>
            </div>
          </div> */}

      <Dialog open={openEdit} onClose={() => setOpenEdit(false)}>
        <DialogTitle className="mt-2 mb-2">Redaguoti pamoką</DialogTitle>
        <DialogContent>
          <form noValidate>
            <DatePicker
              className="mb-3 mt-2"
              label="Pradžios data"
              labelId="add-date"
              fullWidth
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

            <div className="mb-3">
              <FormLabel id="demo-radio-buttons-group-label"></FormLabel>

              <FormControlLabel
                value="end"
                control={
                  <Checkbox
                    checked={repeats}
                    onChange={handleChange}
                    inputProps={{ "aria-label": "controlled" }}
                  />
                }
                label="Pakeisti visoms šio dalyko pamokoms"
                labelPlacement="end"
              />
            </div>
          </form>
        </DialogContent>
        <DialogActions>
          <button className="btn btn-primary mb-2" onClick={editLesson}>
            Redaguoti
          </button>
          <button
            className="btn btn-danger me-3 mb-2"
            onClick={() => setOpenEdit(false)}
          >
            Atšaukti
          </button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default EditLessonPage;
