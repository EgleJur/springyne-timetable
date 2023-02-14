import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";
import { ModulesForSubjects } from "../components/ModulesForSubjects";


function EditSubjectPage() {
  const [subject, setSubject] = useState({});
  const [description, setDescription] = useState("");
  const [nameError, setNameError] = useState("");
  const [module, setModule] = useState({});
  const [room, setRoom] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/subjects/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
  }, [params.id]);

  const editsubject = (e) => {
    e.preventDefault();
    setNameError(false);
    if (subject.name == "") {
      setNameError(true);
    } else {
      fetch("/api/v1/subjects/edit/" + params.id, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(subject),
      }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setSubject({
      ...subject,
      [property]: event.target.value,
    });
  };


  // const assignModuleToSubject = () => {
  //   fetch(`api/v1/subjects/test?sudId=${params.id}&modId=${selectModules}`, {
  //     method: 'POST',
  //     headers: {
  //       'Content-Type': 'application/json'
  //     }
  //   }).then(response => response.json())
  //     .then((subject) => params.onSubjectChange(subject));
  // };

  const selectModules = (event) => {
    setModule(event.target.value)
  }
  const selectRoom = (event) => {
    setRoom(event.target.value)
  }

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti dalyką</h2>
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
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={subject.name}
          id="create-subject-number-with-error"
          label="Pavadinimas"
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}
          required
        />
        <TextField
          onChange={(e) => updateProperty("description", e)}
          value={subject.description}
          id="create-subject-number-with-error"
          label="Aprašymas"
          className="form-control mb-3"
          size="small"
          InputLabelProps={{ shrink: true }}

        />

        <label htmlFor="page-size-select" className="mb-3">
          Modulis:
        </label>
        <ModulesForSubjects id={params.id} onModuleChange={selectModules} />


        <label htmlFor="page-size-select" className="mb-3">
          Kabinetas:
        </label>
        <ModulesForSubjects id={params.id} onModuleChange={selectRoom} />

        <button type="submit" className="btn btn-primary" onClick={editsubject}>
          Redaguoti
        </button>
      </form>
    </div>
  );
}

export default EditSubjectPage;
