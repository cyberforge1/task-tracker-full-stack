// App.tsx

import React from "react";
import "./App.scss";
import TodoContainer from "./containers/TodoContainer/TodoContainer";
import ContainerWrapper from "./containers/ContainerWrapper/ContainerWrapper";

function App() {
  return (
    <ContainerWrapper>
      <TodoContainer />
    </ContainerWrapper>
  );
}

export default App;
