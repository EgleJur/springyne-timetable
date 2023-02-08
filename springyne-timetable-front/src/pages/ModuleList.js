import { useState, useEffect } from "react";

function ModuleListPage() {

    const [ modules, setModules] = useState([]);

    useEffect(() => {
      fetch("/api/v1/modules")
        .then((response) => response.json())
        .then((jsonResponse) => setModules(jsonResponse));
    }, []);

    return (
      <div className="mx-3">
        <h2 className="my-5">Moduliai</h2>
        <button className="btn btn-outline-success mb-5">Pridėti naują modulį</button>
        <table className="table table-hover table-sm">
          <thead className="table-light">
            <tr>
              <th>Numeris</th>
              <th>Pavadinimas</th>
              <th>Veiksmai</th>
            </tr>
          </thead>
          <tbody className="table-group-divider">
            {modules.map((module) => (
              <tr key={module.id}>
                <td>{module.number}</td>
                <td>{module.name}</td>
                <td>
                  <button className="btn btn-outline-secondary">Žiūrėti</button>
                  <button className="btn btn-outline-secondary ms-2">
                    Redaguoti
                  </button>
                  <button className="btn btn-outline-danger ms-2">
                    Ištrinti
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
}

export default ModuleListPage;