import { useState, useEffect } from "react";
import { Alert, Collapse } from "@mui/material";
import { TextField } from "@mui/material";
import { FormControl, MenuItem, Select, InputLabel } from "@mui/material";
import { apiUrl } from "../../App";

function CreateProgramPage() {
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [nameError, setNameError] = useState("");
  const [descriptionError, setDescriptionError] = useState("");
  const [hoursError, setHoursError] = useState("");
  const [subjectError, setSubjectError] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [hours, setHours] = useState("");
  const [subjects, setSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState("");

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/subjects/`)
      .then((response) => response.json())
      .then(setSubjects);
  }, []);

  const createNewProgram = (e) => {
    e.preventDefault();
    setNameError(false);
    setDescriptionError(false);
    setHoursError(false);
    setSubjectError(false);
    if (
      name === "" ||
       description === "" ||
      hours === "" ||
      selectedSubject === ""
    ) {
      if (name === "") {
        setNameError(true);
      }
       if (description === "") {
         setDescriptionError(true);
       }
      if (hours === "") {
        setHoursError(true);
      }
      if (selectedSubject === "") {
        setSubjectError(true);
      }
    } else {
      fetch(
        `${apiUrl}/api/v1/programs?subjectId=${selectedSubject}&hours=${hours}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            name,
            description,
          }),
        }
      ).then((result) => {
        if (result.ok) {
          setName("");
          setDescription("");
          setHours("");
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
      <h2 className="my-5">Pridėti naują programą</h2>
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
          id="create-program-name-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!descriptionError}
          onChange={(e) => setDescription(e.target.value)}
          value={description}
          id="create-teacher-description-error"
          label="Aprašymas"
          helperText="Aprašymas privalomas"
          className="form-control mb-3"
          size="small"
          required
        />
        <TextField
          error={!!hoursError}
          onChange={(e) => setHours(e.target.value)}
          value={hours}
          id="create-subject-hours-with-error"
          label="Dalyko valandų skaičius"
          helperText="Dalyko valandų skaičius privalomas"
          className="form-control mb-3"
          size="small"
          required
        />

        <FormControl fullWidth size="small" className="mb-5">
          <InputLabel id="select-subject-label" error={!!subjectError} required>
            Pasirinkite dalyką
          </InputLabel>
          <Select
            error={!!subjectError}
            labelId="select-subject-label"
            InputLabelProps={{ shrink: true }}
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

        <button
          type="submit"
          className="btn btn-primary"
          onClick={createNewProgram}
        >
          Pridėti
        </button>
      </form>
    </div>
  );
}
export default CreateProgramPage;
