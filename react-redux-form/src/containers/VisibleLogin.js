import { connect } from "react-redux";
import { dataSubmit } from "../actions/loginAction";
import Login from "../components/Login/Login";

// Map Redux state to component props
function mapStateToProps(state) {
    return {
        isLogin: state.LoginReducer.isLogin,
    };
}

// Map Redux actions to component props
function mapDispatchToProps(dispatch) {
    return {
        submitData: data => dispatch(dataSubmit(data)),
    };
}

// Connected Component
const VisibleLogin = connect(
    mapStateToProps,
    mapDispatchToProps
)(Login);

export default VisibleLogin;
