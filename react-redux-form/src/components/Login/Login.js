import React, { Component } from "react";
import PropTypes from "prop-types";
import { Field, reduxForm } from "redux-form";
import { connect } from "react-redux";
import "./auth-form.scss";
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import Home from "../Home/Home";
import { Redirect } from "react-router-dom";

const validate = values => {
    const errors = {};
    if (!values.lpassword) {
        errors.lpassword = "Password is required.";
    } else if (values.lpassword.length > 8) {
        errors.lpassword = "Must be 8 characters or less";
    }

    if (!values.lemail) {
        errors.lemail = "Email is required.";
    } else if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(values.lemail)) {
        errors.lemail = "Invalid email address";
    }

    return errors;
};

const renderField = ({ input, className, label, type, meta: { touched, error, warning } }) => (
    <div>
        <input {...input} className={className} placeholder={label} type={type} />
        {touched && ((error && <span className="text-danger">{error}</span>) || (warning && <span>{warning}</span>))}
    </div>
);

class Login extends Component {
    constructor(props) {
        super(props);

        this.state = {
            lemail: "",
            lpassword: "",
        };
    }

    login = event => {
        let inputName = event.target.name;
        let inputValue = event.target.value;

        let loginCopy = { ...this.state };

        loginCopy[inputName] = inputValue;

        this.setState(loginCopy);
    };

    submit = event => {
        event.preventDefault();
        this.props.submitData(this.state);
    };

    render() {
        const { submitData, isLogin, pristine, reset, submitting, invalid } = this.props;
        if (isLogin) {
            console.log("Login successfully......", this.props);
            this.props.history.push("state", this.state);
            return (
                <Redirect
                    to={{
                        pathname: "/home",
                        state: { from: this.state.lemail },
                    }}
                />
            );
        }
        return (
            <div className="row d-flex justify-content-center no-gutters bg-secondary" id="login-page">
                <div className="col-xl-4 col-lg-5 col-md-6 col-10">
                    <div className="my-5">
                        <div className="pb-4 hb-login-reg-wrapper">
                            <form onSubmit={this.submit} method="POST" className="m-t mx-auto">
                                <h2 className="mb-3 text-center font-weight-normal">Log In</h2>
                                <div className="mb-3">
                                    <div className="row d-flex justify-content-center">
                                        <div className="col-sm-12">
                                            <Field
                                                id="login-email"
                                                name="lemail"
                                                type="email"
                                                className="form-control"
                                                component={renderField}
                                                label="Email"
                                                onChange={this.login}
                                            />
                                        </div>
                                    </div>
                                </div>
                                <div className="mb-3">
                                    <Field
                                        id="login-password"
                                        name="lpassword"
                                        type="password"
                                        className="form-control"
                                        component={renderField}
                                        label="Password"
                                        onChange={this.login}
                                    />
                                </div>
                                <div className="form-group float-left">
                                    <a href="javascript:void(0)" className="forgot-pass">
                                        {/* Forgot Password? */}
                                    </a>
                                </div>

                                <div className="hb-social-icons-wrapper mobile-mx-1">
                                    <button
                                        type="submit"
                                        disabled={submitting | invalid}
                                        id="login-btn"
                                        className="btn p-0"
                                    >
                                        <div className="login-register-bg-btn social-bg bg-primary d-flex justify-content-between align-items-center hb-fix-btn m-0 hb-login-fix-hieght">
                                            <div className="float-left social-icon-bg login-register-bg hb-login-fix-hieght">
                                                <i className="fa fa-check" aria-hidden="true" />
                                            </div>
                                            <div className="hb-btn-text login-register mx-auto" id="btnLogin">
                                                Login into Redux-form
                                                <i
                                                    id="loading-submit"
                                                    className="fa fa-circle-o-notch fa-spin ml-3 p-0 d-none"
                                                />
                                            </div>
                                        </div>
                                    </button>

                                    <div className="align-items-start pt-1 " id="verification-error" />
                                </div>
                            </form>

                            <div className="d-flex justify-content-center">
                                &nbsp; <Link to="/register">Sign Up Here</Link>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default reduxForm({ form: "login", validate })(Login);
