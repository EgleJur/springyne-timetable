import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { Select, MenuItem, FormControl, OutlinedInput, InputLabel } from "@mui/material";

function CreateTeacherPage() {
  const [name, setName] = useState("");
  const [teamsEmail, setTeamsEmail] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [hours, setHours] = useState("");
  const [nameError, setNameError] = useState(false);
  const [teamsEmailError, setTeamsEmailError] = useState(false);
  const [subjectError, setSubjectError] = useState(false);
  const [shiftError, setShiftError] = useState(false);
  const [hoursError, setHoursError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [shifts, setShifts] = useState([]);
  const [selectedShift, setSelectedShift] = useState("");
  const [subjects, setSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState("");

  useEffect(() => {
    fetch("api/v1/shifts/")
      .then((response) => response.json())
      .then(setShifts);
  }, []);

  useEffect(() => {
    fetch("api/v1/subjects/")
      .then((response) => response.json())
      .then(setSubjects);
  }, []);

  const createNewTeacher = (e) => {
    e.preventDefault();
    setNameError(false);
    setTeamsEmailError(false);
    setHoursError(false);
    setShiftError(false);
    setSubjectError(false);
    if (
      name === "" ||
      teamsEmail === "" ||
      hours === "" ||
      selectedSubject === "" ||
      selectedShift === ""
    ) {
      if (name === "") {
        setNameError(true);
      }
      if (teamsEmail === "") {
        setTeamsEmailError(true);
      }
      if (hours === "") {
        setHoursError(true);
      }
      if (selectedSubject === "") {
        setSubjectError(true)
      }
      if (selectedShift === "") {
        setShiftError(true);
      }
    } else {
      fetch(
        `/api/v1/teachers?shiftId=${selectedShift}&subjectId=${selectedSubject}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name,
            teamsEmail,
            email,
            phone,
            hours,
          }),
        }
      ).then((result) => {
        if (result.ok) {
          setName("");
          setTeamsEmail("");
          setEmail("");
          setPhone("");
          setHours("");
          setSelectedShift("");
          setSelectedSubject("");
          setSuccess(true);
          setFailure(false);
          setTimeout(() => {
            setSuccess(false);
                   }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
                   }, 5000);
        }
      });
    }
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Pridėti naują mokytoją</h2>
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
      <form noValidate>
        <TextField
          error={!!nameError}
          onChange={(e) => setName(e.target.value)}
          value={name}
          id="create-teacher-name-with-error"
          label="Vardas ir Pavardė"
          helperText="Vardas ir Pavardė privalomi"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!teamsEmailError}
          onChange={(e) => setTeamsEmail(e.target.value)}
          value={teamsEmail}
          id="create-teacher-teams_mail-with-error"
          label="Teams vartotojo vardas"
          helperText="Teams vardas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          // error={!!emailError}
          onChange={(e) => setEmail(e.target.value)}
          value={email}
          id="create-teacher-email-with-error"
          label="Kontaktinis el. paštas"
          className="form-control mb-3"
          size="small"
        />
        <TextField
          onChange={(e) => setPhone(e.target.value)}
          value={phone}
          id="create-teacher-phone-with-error"
          label="Kontaktinis telefonas"
          className="form-control mb-3"
          size="small"
        />
        <TextField
          error={!!hoursError}
          onChange={(e) => setHours(e.target.value)}
          value={hours}
          id="create-teacher-hours-with-error"
          label="Valandų skaičius per savaitę"
          helperText="Valandų skaičius privalomas"
          className="form-control mb-3"
          size="small"
          required
        />

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-subject-label" error={subjectError} required>
            Pasirinkite dalyką
          </InputLabel>
          <Select
            error={subjectError}
            labelId="select-subject-label"
            id="add-select-subject"
            label="Pasirinkite dalyką"
            fullWidth
            value={selectedSubject}
            onChange={(e) => setSelectedSubject(e.target.value)}
            required
          >
            {subjects?.map((subject) => (
              <MenuItem
                value={subject.id}
                key={subject.id}
                disabled={subject.deleted}
              >
                {subject.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <FormControl fullWidth size="small" className="mb-3">
          <InputLabel id="select-shift-label" error={subjectError} required>
            Pasirinkite pamainą
          </InputLabel>
          <Select
            error={subjectError}
            labelId="select-shift-label"
            id="select-shift"
            label="Pasirinkite pamainą"
            fullWidth
            value={selectedShift}
            onChange={(e) => setSelectedShift(e.target.value)}
            required
          >
            {shifts?.map((subject) => (
              <MenuItem value={subject.id} key={subject.id}>
                {subject.name}
              </MenuItem>
            ))}
          </Select>
        </FormControl>

        <button
          type="submit"
          className="btn btn-primary mb-5"
          onClick={createNewTeacher}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}

export default CreateTeacherPage;
