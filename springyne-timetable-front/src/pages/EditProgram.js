import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  TextField,
  FormControl,
  Select,
  MenuItem,
  InputLabel,
  // Tooltip,
} from "@mui/material";

function EditProgramPage() {
  const [program, setProgram] = useState({});
  const [nameError, setNameError] = useState(false);
  const [descriptionError, setDescriptionError] = useState(false);
  const [hoursError, setHoursError] = useState(false);
  const [subjectError, setSubjectError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [subjects, setSubjects] = useState([]);
  const [hours, setHours] = useState("");
  const [selectedSubject, setSelectedSubject] = useState("");
  const params = useParams();

  const fetchProgram = () => {
    fetch("/api/v1/programs/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setProgram(jsonResponse));
  };

  useEffect(() => fetchProgram, []);

  useEffect(() => {
    fetch("api/v1/subjects/")
      .then((response) => response.json())
      .then(setSubjects);
  }, []);

  const deleteSubject = (e) => {
    fetch(`/api/v1/programs/${params.id}/removeSubject/${e}`, {
      method: "PATCH",
    }).then(fetchProgram);
  };

  const editProgram = (e) => {
    e.preventDefault();
    setNameError(false);
    setDescriptionError(false);
    setHoursError(false);
    setSubjectError(false);
    if (
      program.name === "" ||
      !!selectedSubject ^ !!hours ||
      program.description === ""
    ) {
      if (program.name === "") {
        setNameError(true);
      }
      if (program.description === "") {
        setDescriptionError(true);
      }
      if (!!selectedSubject ^ !!hours) {
        if (hours === "") {
          setHoursError(true);
        }
        if (selectedSubject === "") {
          setSubjectError(true);
        }
      }
    } else {
      fetch(
        `/api/v1/programs/update/${params.id}?&subjectId=${selectedSubject}&hours=${hours}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(program),
        }
      ).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          fetchProgram();
          setSelectedSubject("");
          setHours("");
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };

  const updateProperty = (property, event) => {
    setProgram({
      ...program,
      [property]: event.target.value,
    });
  };

  const handleDelete = () => {
    fetch(`/api/v1/programs/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setProgram(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setSubjectError(false);
    setHoursError(false);
  };
  const handleRestore = () => {
    fetch(`/api/v1/programs/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setProgram(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setSubjectError(false);
    setHoursError(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti programą</h2>

      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
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
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>

      <form noValidate>
        <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
          <tbody>
            <tr>
              <th scope="col">
                <label htmlFor="program-name-with-error">Pavadinimas *</label>
              </th>
              <td>
                <TextField
                  error={!!nameError}
                  onChange={(e) => updateProperty("name", e)}
                  value={program.name}
                  fullWidth
                  id="program-name-with-error"
                  // label="Pavadinimas"
                  helperText="Pavadinimas privalomas"
                  size="small"
                  disabled={program.deleted}
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>

            <tr>
              <th scope="col">
                <label htmlFor="program-description">Aprašymas *</label>
              </th>
              <td>
                <TextField
                  error={!!descriptionError}
                  onChange={(e) => updateProperty("description", e)}
                  value={program.description}
                  multiline
                  helperText="Aprašymas privalomas"
                  id="program-description"
                  // label="Aprašymas"
                  fullWidth
                  size="small"
                  disabled={program.deleted}
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>

            <tr>
              <th scope="col">
                {program.subjects?.length === 0 ? (
                  ""
                ) : (
                  <div className="mb-2">Ištrinti dalyką:</div>
                )}
              </th>
              <td>
                {program.subjects?.map((subject) => (
                  <tr>
                    <button
                      type="submit"
                      className="btn btn-light mb-2"
                      value={subject.subject.id}
                      onClick={(e) => deleteSubject(e.target.value)}
                      key={subject.subject.id}
                      id={subject.subject.id}
                      disabled={program.deleted}
                    >
                      {subject.subject.name} {subject.hours} valandų
                    </button>
                  </tr>
                ))}
              </td>
            </tr>

            <tr>
              <th scope="col">
                <label htmlFor="add-subject-select">Pridėti dalyką</label>
              </th>
              <td>
                {/* <FormControl fullWidth size="small"> */}
                {/* <InputLabel id="select-subject-label" error={!!subjectError}>
                  Pridėti dalyką
                </InputLabel> */}
                <Select
                  error={!!subjectError}
                  disabled={program.deleted}
                  size="small"
                  // labelId="select-subject-label"
                  // InputLabelProps={{ shrink: true }}
                  id="add-subject-select"
                  // label="Pridėti dalyką"
                  fullWidth
                  value={selectedSubject}
                  onChange={(e) => setSelectedSubject(e.target.value)}
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
                {/* </FormControl> */}
              </td>
            </tr>

            <tr>
              <th scope="col">
                <label htmlFor="hours-with-error">Valandų skaičius</label>
              </th>
              <td>
                <TextField
                  error={!!hoursError}
                  onChange={(e) => setHours(e.target.value)}
                  value={hours}
                  id="hours-with-error"
                  // label="Valandų skaičius"
                  fullWidth
                  size="small"
                  disabled={program.deleted}
                />
              </td>
            </tr>
            <tr>
              <th>Būsena</th>
              <td>{program.deleted ? "Ištrintas" : ""}</td>
            </tr>

            <tr>
              <th scope="col">Paskutinį kartą modifikuotas</th>
              <td>{program.modifiedDate}</td>
            </tr>
          </tbody>
        </table>

        <button
          type="submit"
          className="btn btn-primary me-2 mt-2 mb-5"
          onClick={editProgram}
          disabled={program.deleted}
        >
          Redaguoti
        </button>
        {program.deleted ? (
          <button
            className="btn btn-secondary me-2 mt-2 mb-5"
            onClick={handleRestore}
          >
            Atstatyti
          </button>
        ) : (
          <button
            className="btn btn-danger me-2 mt-2 mb-5"
            onClick={handleDelete}
          >
            Ištrinti
          </button>
        )}
      </form>
    </div>
  );
}
export default EditProgramPage;
